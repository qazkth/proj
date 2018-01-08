/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streams;

import common.MyUtils;
import integration.TemperatureDbHandler;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import common.DataCounter;
import common.MyConstants;
import common.TemperaturePojo;
import common.TemperatureStatiticsDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStore;

/**
 *
 * @author Oscar
 */
public class TemperatureStream implements Runnable {
    private final String APPLICATION_ID = "streams-temperature";
    private final String BROKERS = "localhost:9092";
    private final String TOPIC = "streams-temperature-readings";
   
    private final String ALL_TIME_STATE_STORE = "all-time-stats";
    private final String YEARLY_WINDOW = "yearly-stats";
    
    private final String ALL_THERMOMETERS = MyConstants.ALL_THERMOMETERS;
    private final String ALL_LAND_THERMOMETERS = MyConstants.ALL_LAND_THERMOMETERS;
    private final String ALL_OCEAN_THERMOMETERS = MyConstants.ALL_OCEAN_THERMOMETERS;
    
    // A year is always the past 365 days, no leap years here.
    private final int PAST_YEAR = MyConstants.MAX_STREAM_YEAR_WINDOW_SIZE;
    
    private final TemperatureDbHandler dbHandler;
    private final Properties props;
    private final Serde<TemperaturePojo> tempPojoSerde;
    private final Serde<DataCounter> counterSerde;
    
    private KafkaStreams streams;
    private final MyUtils myUtils = new MyUtils();
    private final TemperatureStreamUtils streamUtils = new TemperatureStreamUtils();
    private final StreamsBuilder builder = new StreamsBuilder();
    private final HashSet<Integer> landThermometers = new HashSet<>();
    private final HashSet<Integer> oceanThermometers = new HashSet<>();
        
    public TemperatureStream(TemperatureDbHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.props = this.streamUtils.getApplicationProperties(this.APPLICATION_ID, this.BROKERS);
        this.tempPojoSerde = this.streamUtils.getCustomJsonSerde(TemperaturePojo.class);
        this.counterSerde = this.streamUtils.getCustomJsonSerde(DataCounter.class);
        initThermometerSets();
    }

    @Override
    public void run() {
        KStream<String, TemperaturePojo> temperatures = 
                builder.<String, TemperaturePojo>stream(TOPIC, Consumed.with(Serdes.String(), tempPojoSerde)
                ).filter((k, v) -> {
                    if(existsInLocationSets(v.thermometer_id)) {
                        return true;
                    } else {
                        initThermometerSets();
                        return existsInLocationSets(v.thermometer_id);
                    }
                }).peek((k, v) -> {
                    dbHandler.addTemp(v.thermometer_id, v.temperature, v.read_time_millies);
                });

        KStream<String, TemperaturePojo> allThermometersToSameKey = 
                temperatures.selectKey((k, v) -> ALL_THERMOMETERS);
        
        KStream<String, TemperaturePojo>[] branchedLandOcean = temperatures.branch(
                (k, v) -> landThermometers.contains(v.thermometer_id),
                (k, v) -> oceanThermometers.contains(v.thermometer_id),
                (k, v) -> true
        );
        
        KStream<String, TemperaturePojo> allLandThermometersToSameKey = 
                branchedLandOcean[0].selectKey((k, v) -> ALL_LAND_THERMOMETERS);
        
        KStream<String, TemperaturePojo> allOceanThermometersToSameKey = 
                branchedLandOcean[1].selectKey((k, v) -> ALL_OCEAN_THERMOMETERS);
        
        KStream<String, TemperaturePojo> mergedStreamsToAggregate = temperatures
                .merge(allThermometersToSameKey)
                .merge(allLandThermometersToSameKey)
                .merge(allOceanThermometersToSameKey);
        
        aggregateAlltimeTable(mergedStreamsToAggregate);
        aggregateYearlyTable(mergedStreamsToAggregate);

        streams = new KafkaStreams(builder.build(), props);
        
        //streams.cleanUp();
        streams.start();
        
        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
    
    private boolean existsInLocationSets(int id) {
        return landThermometers.contains(id) || oceanThermometers.contains(id);
    }
    
    private void initThermometerSets() {
        // this is supposed to represent the streaming of the database thermometers to this class,
        // but to reduce the amount of work form me to do I just fetch them manually
        landThermometers.addAll(dbHandler.getLandThermometers());
        oceanThermometers.addAll(dbHandler.getOceanThermometers());        
    }

    private void aggregateAlltimeTable(KStream<String, TemperaturePojo> stream) {        
        stream.groupByKey(Serialized.with(Serdes.String(), this.tempPojoSerde))
            .aggregate(
                () -> new DataCounter(), 
                (aggKey, newValue, aggValue) -> myUtils.myDataCounterAggregator(newValue, aggValue),
                Materialized.<String, DataCounter, KeyValueStore<Bytes, byte[]>>as(this.ALL_TIME_STATE_STORE)
                    .withKeySerde(Serdes.String())
                    .withValueSerde(this.counterSerde)
            );
    }
    
    private void aggregateYearlyTable(KStream<String, TemperaturePojo> stream) {
        stream.groupByKey(Serialized.with(Serdes.String(), this.tempPojoSerde)).
            windowedBy(
                TimeWindows.of(TimeUnit.DAYS.toMillis(PAST_YEAR)).advanceBy(TimeUnit.DAYS.toMillis(1))
            ).aggregate(
                () -> new DataCounter(),
                (aggKey, newValue, aggValue) -> myUtils.myDataCounterAggregator(newValue, aggValue),
                Materialized.<String, DataCounter, WindowStore<Bytes, byte[]>>as(this.YEARLY_WINDOW)
                    .withKeySerde(Serdes.String())
                    .withValueSerde(this.counterSerde)
            );
    }

    public List<? extends TemperatureStatiticsDTO> getWindowedData(String key, Integer days) throws Exception {
        List<DataCounter> windowedCounters = new ArrayList<>();
        long keyForDesiredPeriod = myUtils.getKeyDateDaysFromNow(days);

        ReadOnlyWindowStore<String, DataCounter> windowStore = 
                streams.store(YEARLY_WINDOW, QueryableStoreTypes.<String, DataCounter>windowStore());
        
        windowStore.fetch(key, keyForDesiredPeriod, keyForDesiredPeriod)
                .forEachRemaining(
                        row -> {
                            row.value.source = MyConstants.SOURCE_STREAM_WINDOW;
                            windowedCounters.add(row.value);
                        }
                );

        validateDataExistence(windowedCounters);
        
        // Used to print store states, only used to demonstrate states in report
        printStoresToStdOut(key);
        
        return windowedCounters;
    }

    public List<? extends TemperatureStatiticsDTO> getAlltimeData(String key) throws Exception {
        List<DataCounter> storedCounters = new ArrayList<>();
        
        ReadOnlyKeyValueStore<String, DataCounter> permStore = 
                streams.store(ALL_TIME_STATE_STORE, QueryableStoreTypes.<String, DataCounter>keyValueStore());

        DataCounter counter = permStore.get(key);
        storedCounters.add(counter);
        
        validateDataExistence(storedCounters);
        
        storedCounters.forEach(dc -> dc.source = MyConstants.SOURCE_STREAM_PERMANENT);
        
        // Used to print store states, only used to demonstrate states in report
        printStoresToStdOut(key);
        
        return storedCounters;
    }

    private void validateDataExistence(List list) throws Exception {
        if(list.isEmpty()) { throw new Exception("EMPTY STORES"); }
    }

    // Only used for demonstration purposes
    private void printStoresToStdOut(String key) {
        ReadOnlyKeyValueStore<String, DataCounter> permStore = 
                streams.store(ALL_TIME_STATE_STORE, QueryableStoreTypes.<String, DataCounter>keyValueStore());
        
        System.out.println("ALL CONTENT FROM PERMANENT ALL TIME STORE");
        permStore.all().forEachRemaining(
                row -> System.out.println("Key: " + row.key + ", Value: " + row.value)
        );
        
        ReadOnlyWindowStore<String, DataCounter> windowStore = 
                streams.store(YEARLY_WINDOW, QueryableStoreTypes.<String, DataCounter>windowStore());
        
        System.out.println("ALL CONTENT FROM WINDOWED STORE FOR THERMOMETER ID: " + key);
        windowStore.fetch(key, 0, Long.MAX_VALUE)
                .forEachRemaining(
                        row -> {
                            System.out.println("Key: " + row.key + " = " + new Date(row.key).toString() + ", Value: " + row.value);
                        }
                );
    }
    
}

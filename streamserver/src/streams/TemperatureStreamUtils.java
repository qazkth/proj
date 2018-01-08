/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streams;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.examples.pageview.JsonPOJODeserializer;
import org.apache.kafka.streams.examples.pageview.JsonPOJOSerializer;

/**
 *
 * @author Oscar
 */
class TemperatureStreamUtils {
    
    Properties getApplicationProperties(String appID, String brokers) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
        
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        return props;
    }
    
    <T> Serde<T> getCustomJsonSerde(Class<T> type) {
        Map<String, Object> serdeProps = new HashMap<>();
        
        final Serializer<T> mySerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", type);
        mySerializer.configure(serdeProps, false);

        final Deserializer<T> myDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", type);
        myDeserializer.configure(serdeProps, false);
        
        return Serdes.serdeFrom(mySerializer, myDeserializer);
    }
    
}
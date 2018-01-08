/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streams;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 *
 * @author Oscar
 */
public class MyKafkaProducer {
    private Producer<String, String> producer;
    private final String brokers = "localhost:9092";
    private final String topic = "streams-temperature-readings";
    private final String thermometerId;

    public MyKafkaProducer(int id) {
        String tId = "";
        
        try {
            tId = Integer.toString(id);
        } catch (Exception ex) {}
        
        this.thermometerId = tId;
        initServerConnection();
    }

    private void initServerConnection() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, this.thermometerId);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        
        this.producer = new KafkaProducer<>(props);
    }
    
    public void send(long time, String tempJson) {
        try {
            // <topic, partition, timestamp, key, value>
            this.producer.send(new ProducerRecord(this.topic, null, time, this.thermometerId, tempJson));
        } catch (Exception ex) {
            System.out.println("error sending: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
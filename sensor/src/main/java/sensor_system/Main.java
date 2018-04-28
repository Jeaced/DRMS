package sensor_system;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import sensor_system.environment.RoomSimulator;
import sensor_system.utils.ResourceSerializer;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ResourceSerializer.class);

        RoomSimulator roomSimulator = new RoomSimulator();
        roomSimulator.setProducerConfig(config);

        roomSimulator.initializeResources();

        roomSimulator.start();

        roomSimulator.addHuman();
        roomSimulator.addHuman();
        roomSimulator.addHuman();
    }
}

package sensor_system;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sensor implements Runnable {
    private Room room;
    private AtomicBoolean stop;

    public Sensor(Room room, AtomicBoolean stop) {
        this.room = room;
        this.stop = stop;
    }

    @Override
    public void run() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        Producer<String, String> myProducer = new KafkaProducer<>(config,
                new StringSerializer(), new StringSerializer());

        while (!stop.get()) {
            myProducer.send(new ProducerRecord<>("RoomResources",
                    "water", Double.toString(room.getWater())));
            myProducer.send(new ProducerRecord<>("RoomResources",
                    "food", Double.toString(room.getFood())));
            myProducer.send(new ProducerRecord<>("RoomResources",
                    "garbage", Double.toString(room.getGarbage())));
            myProducer.send(new ProducerRecord<>("RoomResources",
                    "paper", Double.toString(room.getToiletPaper())));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("got interrupted");
            }
        }
    }
}

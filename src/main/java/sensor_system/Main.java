package sensor_system;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Room room = new Room();
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        Producer<String, String> myProducer = new KafkaProducer<>(config,
                new StringSerializer(), new StringSerializer());
        AtomicBoolean stop = new AtomicBoolean(false);
        Thread sensor = new Thread(new Sensor(room, stop));
        sensor.start();
        Thread human = new Thread(new Human(room, stop));
        human.start();
        String readValue, value;
        Double parsedValue;
        while (true) {
            readValue = sc.nextLine();
            if (readValue.equals("stop")) {
                break;
            } else if (readValue.equals("garbage")) {
                room.takeOutGarbage();
                myProducer.send(new ProducerRecord<>("Tasks",
                        "garbage", null));
            } else if (readValue.startsWith("water ")) {
                value = readValue.substring(6);
                try {
                    parsedValue = Double.parseDouble(value);
                    room.addWater(parsedValue);
                    myProducer.send(new ProducerRecord<>("Tasks",
                            "water", null));
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect format");
                }
            } else if (readValue.startsWith("food ")) {
                value = readValue.substring(5);
                try {
                    parsedValue = Double.parseDouble(value);
                    room.addFood(parsedValue);
                    myProducer.send(new ProducerRecord<>("Tasks",
                            "food", null));
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect format");
                }
            } else if (readValue.startsWith("paper ")) {
                value = readValue.substring(6);
                try {
                    parsedValue = Double.parseDouble(value);
                    room.addToiletPaper(parsedValue);
                    myProducer.send(new ProducerRecord<>("Tasks",
                            "paper", null));
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect format");
                }
            } else {
                System.out.println("Unknown command");
            }
        }

        stop.set(true);
    }
}

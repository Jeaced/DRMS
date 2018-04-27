package sensor_system.environment;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import sensor_system.resources.Resource;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sensor extends Thread {
    private Resource resource;
    private AtomicBoolean stop;
    private Map<String, Object> producerConfig;

    public Sensor(Resource resource, AtomicBoolean stop) {
        this.resource = resource;
        this.stop = stop;
    }

    public void setProducerConfig(Map<String, Object> producerConfig) {
        this.producerConfig = producerConfig;
    }

    @Override
    public void run() {
        Producer<String, Double> sensorProducer
                = new KafkaProducer<>(producerConfig);

        System.out.println(Thread.currentThread().getName());

        while (!stop.get()) {
            sensorProducer.send(new ProducerRecord<>("RoomResources",
                    resource.getName(), resource.getValue()));
            System.out.println(resource);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("got interrupted");
            }
        }
    }
}

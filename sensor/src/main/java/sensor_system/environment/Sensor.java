package sensor_system.environment;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sensor_system.resources.Resource;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sensor extends Thread {
    private final static Logger log = LogManager.getLogger(Sensor.class);

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
        Producer<String, Resource> sensorProducer = new KafkaProducer<>(producerConfig);

        System.out.println(Thread.currentThread().getName());

        while (!stop.get()) {
            sensorProducer.send(new ProducerRecord<>("RoomResources",
                    resource.getName(), resource));
            log.debug(resource);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error(e);
                Thread.currentThread().interrupt();
            }
        }
    }
}

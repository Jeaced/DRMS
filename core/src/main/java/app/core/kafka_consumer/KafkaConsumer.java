package app.core.kafka_consumer;

import app.core.DAO.ResourceDAO;
import app.core.models.Resource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumer {
    @Autowired
    ResourceDAO resourceDAO;

    @KafkaListener(id = "batch-listener", topics = {"RoomResources"})
    public void receive(@Payload List<ConsumerRecord<String, Double>> messages) {
        for (ConsumerRecord<String, Double> cr : messages) {
            if (cr.topic().equals("RoomResources")) {
                String resourceName = cr.key();
                Double resourceValue = cr.value();

                Resource resource = resourceDAO.findByName(resourceName);

                if (resource == null) {
                    resource = new Resource();
                    resource.setName(resourceName);
                }

                resource.setValue(resourceValue);
                resourceDAO.save(resource);
            }
        }
    }
}
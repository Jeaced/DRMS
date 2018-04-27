package app.core.kafka_consumer;

import app.core.models.Resource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumer {

    @KafkaListener(id = "batch-listener", topics = {"RoomResources", "Tasks"})
    public void receive(@Payload List<ConsumerRecord<String, Resource>> messages) {

        for (ConsumerRecord<String, Resource> cr : messages) {
            if (cr.topic().equals("RoomResources")) {
                System.out.println(cr.value());
            }
        }
    }
}
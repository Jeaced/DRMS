package app.core.kafka_consumer;

import app.core.DAO.ResourceDAO;
import app.core.DAO.TaskDAO;
import app.core.models.Resource;
import app.core.utils.TaskGenerator;
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

    @Autowired
    TaskDAO taskDAO;

    @Autowired
    TaskGenerator taskGenerator;

    @KafkaListener(id = "batch-listener", topics = {"RoomResources"})
    public void receive(@Payload List<ConsumerRecord<String, Resource>> messages) {
        for (ConsumerRecord<String, Resource> cr : messages) {
            if (cr.topic().equals("RoomResources")) {
                Resource resource = cr.value();
                resource.setHasTask(false);

                Resource resourceOld = resourceDAO.findByName(resource.getName());
                if (resourceOld != null && !resourceOld.isHasTask()) {
                    if (taskGenerator.isCriticalResource(resource)) {
                        taskDAO.save(taskGenerator.generate(resource));
                        resource.setHasTask(true);
                    }
                }

                resourceDAO.save(resource);
            }
        }
    }
}
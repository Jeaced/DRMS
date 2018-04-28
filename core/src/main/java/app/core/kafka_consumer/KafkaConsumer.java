package app.core.kafka_consumer;

import app.core.DAO.ResourceDAO;
import app.core.DAO.TaskDAO;
import app.core.DAO.UserDAO;
import app.core.models.Resource;
import app.core.models.Task;
import app.core.models.TaskStatus;
import app.core.models.User;
import app.core.utils.TaskGenerator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class KafkaConsumer {
    private final static Logger log = LogManager.getLogger(KafkaConsumer.class);

    @Autowired
    ResourceDAO resourceDAO;

    @Autowired
    TaskDAO taskDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    TaskGenerator taskGenerator;

    @KafkaListener(id = "batch-listener", topics = {"RoomResources"})
    public void receive(@Payload List<ConsumerRecord<String, Resource>> messages) {
        List<User> users = userDAO.findAll();
        Random random = new Random();

        for (ConsumerRecord<String, Resource> cr : messages) {
            if (cr.topic().equals("RoomResources")) {
                Resource resource = cr.value();
                Resource resourceOld = resourceDAO.findById(resource.getId()).orElse(new Resource());
                resource.setTask(resourceOld.getTask());
                resourceDAO.save(resource);

                if (taskGenerator.isCriticalResource(resource) && resourceOld.getTask() == null) {
                    log.info("Generating new task");

                    Task task = taskGenerator.generate(resource);
                    task.setResource(resource);

                    if (users.size() > 0) {
                        task.setUser(users.get(random.nextInt(users.size())));
                        task.setStatus(TaskStatus.ASSIGNED);
                    } else {
                        task.setStatus(TaskStatus.NEW);
                    }

                    taskDAO.save(task);
                    resource.setTask(task);
                }
                else if (!taskGenerator.isCriticalResource(resource) && resourceOld.getTask() != null) {
                    Optional<Task> taskOptional = taskDAO.findById(resourceOld.getTask().getId());
                    if (taskOptional.isPresent()) {
                        Task task = taskOptional.get();
                        if (task.getStatus() == TaskStatus.ASSIGNED || task.getStatus() == TaskStatus.NEW) {
                            log.info("Finishing the task");
                            task.setStatus(TaskStatus.FINISHED);
                            task.setFinished(LocalDateTime.now());
                            taskDAO.save(task);
                            resource.setTask(null);
                        }
                    }
                }

                resourceDAO.save(resource);
            }
        }
    }
}
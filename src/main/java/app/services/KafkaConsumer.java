package app.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumer {

    private final double MAX_GARBAGE = 5;
    private final double MIN_WATER = 0.1;
    private final double MIN_PAPER = 0.1;
    private final double MIN_FOOD = 0.1;

    @KafkaListener(id = "batch-listener", topics = {"RoomResources", "Tasks"})
    public void receive(@Payload List<ConsumerRecord<String, String>> messages) {

        for (ConsumerRecord<String, String> cr : messages) {
            if (cr.topic().equals("RoomResources")) {
                switch (cr.key()) {
                    case "garbage":
                        if (Double.parseDouble(cr.value()) > MAX_GARBAGE) {
                            //todo Create new task and assign to user who didn't have tasks for the longest period
                        }
                        break;
                    case "water":
                        if (Double.parseDouble(cr.value()) < MIN_WATER) {
                            //todo Create new task and assign to user who didn't have tasks for the longest period
                        }
                        break;
                    case "food":
                        if (Double.parseDouble(cr.value()) < MIN_FOOD) {
                            //todo Create new task and assign to user who didn't have tasks for the longest period
                        }
                        break;
                    case "paper":
                        if (Double.parseDouble(cr.value()) < MIN_PAPER) {
                            //todo Create new task and assign to user who didn't have tasks for the longest period
                        }
                        break;
                }
            } else {
                switch (cr.key()) {
                    case "garbage":
                        //todo Find current garbage task and set status to done
                        break;
                    case "water":
                        //todo Find current water task and set status to done
                        break;
                    case "food":
                        //todo Find current food task and set status to done
                        break;
                    case "paper":
                        //todo Find current paper task and set status to done
                        break;
                }
            }
        }
    }
}
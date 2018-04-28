package app.core.utils;

import app.core.models.Resource;
import app.core.models.Task;
import app.core.models.TaskStatus;
import app.core.models.TaskType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class TaskGenerator {
    public boolean isCriticalResource(Resource resource) {
        if (resource.isExpiring()) {
            return resource.getValue() <= resource.getCriticalThreshold();
        } else {
            return resource.getValue() >= resource.getCriticalThreshold();
        }
    }

    public Task generate(Resource resource) {
        Map<String, TaskType> tasksTypes = new HashMap<>();

        /* Task types */
        tasksTypes.put("Water", TaskType.WATER);
        tasksTypes.put("Toilet Paper", TaskType.TOILET_PAPER);
        tasksTypes.put("Bread", TaskType.FOOD);
        tasksTypes.put("Fruits", TaskType.FOOD);
        tasksTypes.put("Vegetables", TaskType.FOOD);
        tasksTypes.put("Meat", TaskType.FOOD);
        tasksTypes.put("Mess", TaskType.MESS);
        tasksTypes.put("Garbage", TaskType.GARBAGE);

        Map<String, List<String>> tasksDescriptionBeginnings = new HashMap<>();
        Map<String, List<String>> tasksDescriptionEndings = new HashMap<>();


        /* Task description beginnings */
        List<String> textsGet = new ArrayList<>();
        textsGet.add("Buy some ");
        textsGet.add("Get some ");

        List<String> textsRemove = new ArrayList<>();
        textsRemove.add("Take out the ");
        textsRemove.add("Remove the ");

        tasksDescriptionBeginnings.put("Water", textsGet);
        tasksDescriptionBeginnings.put("Toilet Paper", textsGet);
        tasksDescriptionBeginnings.put("Bread", textsGet);
        tasksDescriptionBeginnings.put("Fruits", textsGet);
        tasksDescriptionBeginnings.put("Vegetables", textsGet);
        tasksDescriptionBeginnings.put("Meat", textsGet);
        tasksDescriptionBeginnings.put("Mess", textsRemove);
        tasksDescriptionBeginnings.put("Garbage", textsRemove);

        /* Task description endings */
        List<String> textsWhere = new ArrayList<>();
        textsWhere.add(" in the shop.");

        List<String> textsFrom = new ArrayList<>();
        textsFrom.add(" from the kitchen.");
        textsFrom.add(" from the toilet room.");
        textsFrom.add(" from the living room.");

        tasksDescriptionEndings.put("Water", textsWhere);
        tasksDescriptionEndings.put("Toilet Paper", textsWhere);
        tasksDescriptionEndings.put("Bread", textsWhere);
        tasksDescriptionEndings.put("Fruits", textsWhere);
        tasksDescriptionEndings.put("Vegetables", textsWhere);
        tasksDescriptionEndings.put("Meat", textsWhere);
        tasksDescriptionEndings.put("Mess", textsFrom);
        tasksDescriptionEndings.put("Garbage", textsFrom);

        /* Generating new task */
        Random random = new Random();
        String name = resource.getName();

        int beginningsLength = tasksDescriptionBeginnings.get(name).size();
        String beginning = tasksDescriptionBeginnings.get(name).get(random.nextInt(beginningsLength));

        int endingsLength = tasksDescriptionEndings.get(name).size();
        String ending = tasksDescriptionEndings.get(name).get(random.nextInt(endingsLength));

        Task task = new Task();
        task.setDescription(beginning + name + ending);
        task.setType(tasksTypes.get(name));
        task.setCreated(LocalDateTime.now());
        task.setStatus(TaskStatus.NEW);

        return task;
    }
}

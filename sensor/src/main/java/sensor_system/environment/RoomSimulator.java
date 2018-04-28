package sensor_system.environment;

import sensor_system.agents.Human;
import sensor_system.resources.Resource;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RoomSimulator extends Thread {
    private Room room;
    private List<Thread> humans;
    private List<Sensor> sensors;
    private AtomicBoolean stop;
    private Map<String, Object> producerConfig;

    public RoomSimulator() {
        this.room = new Room();
        this.humans = new ArrayList<>();
        this.sensors = new ArrayList<>();
        stop = new AtomicBoolean(false);
    }

    public void setProducerConfig(Map<String, Object> producerConfig) {
        this.producerConfig = producerConfig;
    }

    public void addResource(Resource resource) {
        this.room.addResource(resource);

        Sensor sensor = new Sensor(resource, stop);
        sensor.setProducerConfig(producerConfig);
        sensor.start();
    }

    public void addResources(List<Resource> resources) {
        for (Resource r : resources) {
            this.room.addResource(r);

            Sensor sensor = new Sensor(r, stop);
            sensor.setProducerConfig(producerConfig);
            sensor.start();
        }
    }

    public void addHuman() {
        Thread humanThread = new Thread(new Human(room, stop));
        this.humans.add(humanThread);

        humanThread.start();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        stop.set(false);

        String command;
        while (true) {
            command = sc.nextLine();

            if (command.equals("stop")) {
                break;
            } else {
                System.out.println("Unknown command");
            }
        }

        stop.set(true);
    }

    public void initializeResources() {
        List<Resource> resources = new ArrayList<>();

        Resource water = new Resource(
                1L, "Water", "liters", 19., 19., 1., true
        );
        Resource toilerPaper = new Resource(
                2L, "Toilet Paper", "rolls", 1., 2., 0.1, true
        );
        Resource bread = new Resource(
                3L, "Bread", "loafs", 1., 1., 0.1, true
        );
        Resource fruits = new Resource(
                4L, "Fruits", "pieces", 5.,10., 1., true
        );
        Resource vegetables = new Resource(
                5L, "Vegetables", "pieces", 5.,10., 1., true
        );
        Resource meat = new Resource(
                6L, "Meat", "pieces", 2.,2., .5, true
        );
        Resource mess = new Resource(
                7L, "Mess", "stacks", 0., 5., false
        );
        Resource garbage = new Resource(
                8L, "Garbage", "packets", 0., 4., false
        );

        resources.add(water);
        resources.add(toilerPaper);
        resources.add(bread);
        resources.add(fruits);
        resources.add(vegetables);
        resources.add(meat);
        resources.add(garbage);
        resources.add(mess);

        this.addResources(resources);
    }
}

package sensor_system.environment;

import sensor_system.agents.Human;
import sensor_system.resources.CumulativeResource;
import sensor_system.resources.ExpiringResource;
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

        Resource water = new ExpiringResource(
                1L, "water", "liters", 19., 19., 1.
        );
        Resource toilerPaper = new ExpiringResource(
                2L, "toiletPaper", "rolls", 1., 2., 0.1
        );
        Resource bread = new ExpiringResource(
                3L, "bread", "loafs", 1., 1., 0.1
        );
        Resource fruits = new ExpiringResource(
                4L, "fruits", "pieces", 5.,10., 1.
        );
        Resource vegetables = new ExpiringResource(
                5L, "vegetables", "pieces", 5.,10., 1.
        );
        Resource meat = new ExpiringResource(
                6L, "meat", "pieces", 2.,2., .5
        );
        Resource mess = new CumulativeResource(
                7L, "mess", "stacks", 0., 5.
        );
        Resource garbage = new CumulativeResource(
                8L, "garbage", "packets", 0., 4.
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

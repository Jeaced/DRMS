package sensor_system.environment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sensor_system.agents.Human;
import sensor_system.resources.Resource;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RoomSimulator extends Thread {
    private final static Logger log = LogManager.getLogger(RoomSimulator.class);

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
        log.info("Starting room simulation");

        Scanner sc = new Scanner(System.in);
        stop.set(false);

        String command;
        while (true) {
            command = sc.nextLine();

            if (command.equals("stop")) {
                break;
            } else if (!restoreResource(command)) {
                System.out.println("Unknown command");
            }
        }

        log.info("Ending room simulation");
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
        resources.add(mess);
        resources.add(garbage);

        this.addResources(resources);
    }

    private boolean restoreResource(String resource) {
        switch (resource.toLowerCase()) {
            case "water":
                this.room.getResources().get(0).get().restore();
                log.info("Restored water resources");
                return true;
            case "toilet paper":
                this.room.getResources().get(1).get().restore();
                log.info("Restored toilet paper resources");
                return true;
            case "bread":
                this.room.getResources().get(2).get().restore();
                log.info("Restored bread resources");
                return true;
            case "fruits":
                this.room.getResources().get(3).get().restore();
                log.info("Restored fruits resources");
                return true;
            case "vegetables":
                this.room.getResources().get(4).get().restore();
                log.info("Restored vegetables resources");
                return true;
            case "meat":
                this.room.getResources().get(5).get().restore();
                log.info("Restored meat resources");
                return true;
            case "mess":
                this.room.getResources().get(6).get().restore();
                log.info("Restored mess resources");
                return true;
            case "garbage":
                this.room.getResources().get(7).get().restore();
                log.info("Restored garbage resources");
                return true;
        }
        return false;
    }
}

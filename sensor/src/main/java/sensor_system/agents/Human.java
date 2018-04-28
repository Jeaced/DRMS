package sensor_system.agents;

import sensor_system.resources.Resource;
import sensor_system.environment.Room;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Human extends Thread {
    private Room room;
    private AtomicBoolean stop;

    public Human(Room room, AtomicBoolean stop) {
        this.room = room;
        this.stop = stop;
    }

    @Override
    public void run() {
        List<AtomicReference<Resource>> availableResources;

        while (!stop.get()) {
            availableResources = room.getResources()
                    .stream()
                    .filter(resource -> !resource.get().isOff())
                    .collect(Collectors.toList());

            int resourceId = ThreadLocalRandom.current().nextInt(availableResources.size());
            AtomicReference<Resource> resource = availableResources.get(resourceId);
            AtomicReference<Resource>  garbage = availableResources.get(availableResources.size() - 1);

            switch (resource.get().getName()) {
                case "Water":
                    resource.get().use(truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 2.)));
                    break;
                case "Toilet Paper":
                    double value = truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 0.1));
                    resource.get().use(value);
                    garbage.get().use(value);
                    break;
                case "Bread":
                    resource.get().use(truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 0.1)));
                    break;
                case "Fruits":
                    if (truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 1.)) > 0.6) {
                        resource.get().use(1.);
                        garbage.get().use(0.1);
                    }
                    break;
                case "Vegetables":
                    if (truncateDouble(ThreadLocalRandom.current().nextDouble(0., 1.)) > 0.6) {
                        resource.get().use(1.);
                        garbage.get().use(0.1);
                    }
                    break;
                case "Meat":
                    resource.get().use(truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 0.4)));
                    garbage.get().use(0.1);
                    break;
                case "Mess":
                    resource.get().use(truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 1.)));
                    break;
                case "Garbage":
                    resource.get().use(truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 1.)));
                    break;
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private double truncateDouble(double value) {
        DecimalFormat df = new DecimalFormat("#.00");
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(df.format(value));
    }
}

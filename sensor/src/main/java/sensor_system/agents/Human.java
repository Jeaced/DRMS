package sensor_system.agents;

import sensor_system.resources.Resource;
import sensor_system.environment.Room;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
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
        List<Resource> availableResources;

        while (!stop.get()) {
            availableResources = room.getResources()
                    .stream()
                    .filter(resource -> !resource.isGone())
                    .collect(Collectors.toList());

            int resourceId = ThreadLocalRandom.current().nextInt(availableResources.size() );
            Resource resource = availableResources.get(resourceId);
            Resource garbage = availableResources.get(availableResources.size() - 1);

            switch (resource.getName()) {
                case "water":
                    resource.use(truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 2.)));
                    break;
                case "toiletPaper":
                    double value = truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 0.1));
                    resource.use(value);
                    garbage.use(value);
                    break;
                case "bread":
                    resource.use(truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 0.1)));
                    break;
                case "fruits":
                    if (truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 1.)) > 0.6) {
                        resource.use(1.);
                        garbage.use(0.1);
                    }
                    break;
                case "vegetables":
                    if (truncateDouble(ThreadLocalRandom.current().nextDouble(0., 1.)) > 0.6) {
                        resource.use(1.);
                        garbage.use(0.1);
                    }
                    break;
                case "meat":
                    resource.use(truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 0.4)));
                    garbage.use(0.1);
                    break;
                case "mess":
                    resource.use(truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 1.)));
                    break;
                case "garbage":
                    resource.use(truncateDouble(ThreadLocalRandom.current().nextDouble(0.01, 1.)));
                    break;
            }

            try {
                Thread.sleep(5000);
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

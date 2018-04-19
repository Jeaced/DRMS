package sensor_system;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Human implements Runnable {
    Room room;
    AtomicBoolean stop;

    public Human(Room room, AtomicBoolean stop) {
        this.room = room;
        this.stop = stop;
    }

    @Override
    public void run() {
        while (!stop.get()) {
            room.eatFood(ThreadLocalRandom.current().nextDouble(0, 1));
            room.addGarbage(ThreadLocalRandom.current().nextDouble(0, 1));
            room.useToiletPaper(ThreadLocalRandom.current().nextDouble(0, 1));
            room.drinkWater(ThreadLocalRandom.current().nextDouble(0, 1));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

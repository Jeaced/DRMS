package sensor_system;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Room room = new Room();
        AtomicBoolean stop = new AtomicBoolean(false);
        Thread sensor = new Thread(new Sensor(room, stop));
        sensor.start();
        Thread human = new Thread(new Human(room, stop));
        human.start();
        String readValue, value;
        Double parsedValue;
        while (true) {
            readValue = sc.nextLine();
            if (readValue.equals("stop")) {
                break;
            } else if (readValue.equals("garbage")) {
                room.takeOutGarbage();
            } else if (readValue.startsWith("water ")) {
                value = readValue.substring(6);
                try {
                    parsedValue = Double.parseDouble(value);
                    room.addWater(parsedValue);
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect format");
                }
            } else if (readValue.startsWith("food ")) {
                value = readValue.substring(5);
                try {
                    parsedValue = Double.parseDouble(value);
                    room.addFood(parsedValue);
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect format");
                }
            } else if (readValue.startsWith("paper ")) {
                value = readValue.substring(6);
                try {
                    parsedValue = Double.parseDouble(value);
                    room.addToiletPaper(parsedValue);
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect format");
                }
            } else {
                System.out.println("Unknown command");
            }
        }

        stop.set(true);
    }
}

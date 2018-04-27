package sensor_system;


public class Room {
    private double water;
    private double food;
    private double garbage;
    private double toiletPaper;
    private Object waterLock;
    private Object foodLock;
    private Object paperLock;
    private Object garbageLock;

    public Room () {
        water = 0.0;
        food = 0.0;
        garbage = 0.0;
        toiletPaper = 0.0;
        waterLock = new Object();
        foodLock = new Object();
        paperLock = new Object();
        garbageLock = new Object();
    }

    public double getWater() {
        synchronized (waterLock) {
            return water;
        }
    }

    public void addWater(double water) {
        synchronized (waterLock) {
            this.water += water;
        }
    }

    public double getFood() {
        synchronized (foodLock) {
            return food;
        }
    }

    public void addFood(double food) {
        synchronized (foodLock) {
            this.food += food;
        }
    }

    public double getToiletPaper() {
        synchronized (paperLock) {
            return toiletPaper;
        }
    }

    public void addToiletPaper(double toiletPaper) {
        synchronized (paperLock) {
            this.toiletPaper += toiletPaper;
        }
    }

    public double getGarbage() {
        synchronized (garbageLock) {
            return garbage;
        }
    }

    public void takeOutGarbage() {
        synchronized (garbageLock) {
            garbage = 0.0;
        }
    }

    public void drinkWater(double water) {
        synchronized (waterLock) {
            this.water -= Double.min(water, this.water);
        }
    }

    public void addGarbage(double garbage) {
        synchronized (garbageLock) {
            this.garbage += garbage;
        }
    }

    public void eatFood(double food) {
        synchronized (foodLock) {
            this.food -= Double.min(food, this.food);
        }
    }

    public void useToiletPaper(double toiletPaper) {
        synchronized (paperLock) {
            this.toiletPaper -= Double.min(toiletPaper, this.toiletPaper);
        }
    }
}

package sensor_system.resources;

public abstract class Resource {
    private Long id;
    private String name;
    private String unit;
    private double value;
    private double criticalThreshold;

    public Resource() {
    }

    public Resource(Long id, String name, String unit, double value, double criticalThreshold) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.value = value;
        this.criticalThreshold = criticalThreshold;
    }

    public Resource(Long id, String name, String unit, double criticalThreshold) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.criticalThreshold = criticalThreshold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getCriticalThreshold() {
        return criticalThreshold;
    }

    public void setCriticalThreshold(double criticalThreshold) {
        this.criticalThreshold = criticalThreshold;
    }

    public abstract void use(double value);

    public abstract void restore();

    public abstract boolean inNormalState();

    public abstract boolean inCriticalState();

    public abstract boolean isGone();

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                ", criticalThreshold=" + criticalThreshold +
                '}';
    }
}

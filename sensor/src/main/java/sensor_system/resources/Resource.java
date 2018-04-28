package sensor_system.resources;

public class Resource {
    private Long id;
    private String name;
    private String unit;
    private double value;
    private double criticalThreshold;
    private double minValue;
    private double maxValue;
    private boolean isExpiring;
    private boolean isOff;

    public Resource() {
    }

    public Resource(Long id, String name, String unit, double value, double criticalThreshold,
                    boolean isExpiring) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.value = value;
        this.criticalThreshold = criticalThreshold;
        this.minValue = 0.;
        this.maxValue = Double.MAX_VALUE;
        this.isExpiring = isExpiring;
        this.isOff = false;
    }

    public Resource(Long id, String name, String unit, double value, double maxValue,
                    double criticalThreshold, boolean isExpiring) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.value = value;
        this.criticalThreshold = criticalThreshold;
        this.minValue = 0.;
        this.maxValue = maxValue;
        this.isExpiring = isExpiring;
        this.isOff = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isExpiring() {
        return isExpiring;
    }

    public void setExpiring(boolean expiring) {
        isExpiring = expiring;
    }

    public boolean isOff() {
        return isOff;
    }

    public void setOff(boolean off) {
        isOff = off;
    }

    public void use(double value) {
        if (isExpiring) {
            if (this.value - value < this.minValue) {
                setValue(this.minValue);
            } else {
                setValue(this.value - value);
            }
        } else {
            setValue(this.value + value);
        }
    }

    public void restore() {
        if (isExpiring) {
            setValue(this.maxValue);
        } else {
            setValue(this.minValue);
        }
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                ", criticalThreshold=" + criticalThreshold +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", isExpiring=" + isExpiring +
                ", isOff=" + isOff +
                '}';
    }
}

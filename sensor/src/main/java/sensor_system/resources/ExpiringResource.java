package sensor_system.resources;

public class ExpiringResource extends Resource {
    private double maxValue;

    public ExpiringResource(Long id, String name, String unit, double value, double maxValue, double criticalThreshold) {
        super(id, name, unit, value, criticalThreshold);
        this.maxValue = maxValue;
    }

    public ExpiringResource(Long id, String name, String unit, double maxValue, double criticalThreshold) {
        super(id, name, unit, criticalThreshold);
        this.maxValue = maxValue;
    }

    @Override
    public void use(double value) {
        double currentValue = getValue();

        if (currentValue - value < 0.) {
            setValue(0.);
        } else {
            setValue(currentValue - value);
        }
    }

    @Override
    public void restore() {
        this.setValue(maxValue);
    }

    @Override
    public boolean inNormalState() {
        return this.getValue() > this.getCriticalThreshold();
    }

    @Override
    public boolean inCriticalState() {
        return this.getValue() <= this.getCriticalThreshold();
    }

    @Override
    public boolean isGone() {
        return getValue() == 0.;
    }
}

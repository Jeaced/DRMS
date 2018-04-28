package sensor_system.resources;

public class CumulativeResource extends Resource {
    public CumulativeResource(Long id, String name, String unit, double value, double criticalThreshold) {
        super(id, name, unit, value, criticalThreshold);
    }

    public CumulativeResource(Long id, String name, String unit, double criticalThreshold) {
        super(id, name, unit, criticalThreshold);
    }

    @Override
    public void use(double value) {
        setValue(getValue() + value);
    }

    @Override
    public void restore() {
        this.setValue(0.);
    }

    @Override
    public boolean inNormalState() {
        return this.getValue() < this.getCriticalThreshold();
    }

    @Override
    public boolean inCriticalState() {
        return this.getValue() >= this.getCriticalThreshold();
    }

    @Override
    public boolean isGone() {
        return false;
    }
}

package sensor_system.environment;

import sensor_system.resources.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Room {
    private List<AtomicReference<Resource>> resources;
    private List<Object> locks;

    public Room() {
        this.resources = new ArrayList<>();
        this.locks = new ArrayList<>();
    }

    public List<AtomicReference<Resource>> getResources() {
        return resources;
    }

    public void setResources(List<AtomicReference<Resource>> resources) {
        this.resources = resources;
    }

    public void addResource(Resource resource) {
        this.resources.add(new AtomicReference<>(resource));
    }
}

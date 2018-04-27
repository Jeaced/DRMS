package sensor_system.environment;


import sensor_system.resources.Resource;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<Resource> resources;
    private List<Object> locks;

    public Room() {
        this.resources = new ArrayList<>();
        this.locks = new ArrayList<>();
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
    }
}

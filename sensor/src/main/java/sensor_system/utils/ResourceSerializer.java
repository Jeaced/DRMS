package sensor_system.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sensor_system.resources.Resource;

import java.util.Map;

public class ResourceSerializer implements Serializer<Resource> {
    private final static Logger log = LogManager.getLogger(ResourceSerializer.class);

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String arg0, Resource arg1) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(arg1).getBytes();
        } catch (Exception e) {
            log.error(e);
        }
        return retVal;
    }

    @Override
    public void close() {
    }
}

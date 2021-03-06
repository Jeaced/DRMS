package app.core.kafka_consumer;

import app.core.models.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ResourceDeserializer implements Deserializer<Resource> {
    private final static Logger log = LogManager.getLogger(ResourceDeserializer.class);

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {

    }

    @Override
    public Resource deserialize(String arg0, byte[] arg1) {
        ObjectMapper mapper = new ObjectMapper();
        Resource resource = null;
        try {
            resource = mapper.readValue(arg1, Resource.class);
        } catch (Exception e) {
            log.error(e);
        }
        return resource;
    }
}

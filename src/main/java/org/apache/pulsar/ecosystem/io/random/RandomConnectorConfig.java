package org.apache.pulsar.ecosystem.io.random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The configuration class for {@link RandomConnector}.
 */
@Getter
@EqualsAndHashCode
@ToString
public class RandomConnectorConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The seed used for creating a RANDOM instance.
     */
    private Long randomSeed;

    /**
     * Max message size for generating the record.
     */
    private Integer maxMessageSize;

    /**
     * Validate if the configuration is valid.
     */
    public void validate() {
        Objects.requireNonNull(maxMessageSize, "No `maxMessageSize` is provided");
    }

    /**
     * Load the configuration from provided properties.
     *
     * @param config property map
     * @return a loaded {@link RandomConnectorConfig}.
     * @throws IOException when fail to load the configuration from provided properties
     */
    public static RandomConnectorConfig load(Map<String, Object> config) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new ObjectMapper().writeValueAsString(config), RandomConnectorConfig.class);
    }


}

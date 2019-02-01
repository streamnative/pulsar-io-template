package org.apache.pulsar.ecosystem.io.random;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.pulsar.functions.api.Record;
import org.apache.pulsar.io.core.Source;
import org.apache.pulsar.io.core.SourceContext;

import static avro.shaded.com.google.common.base.Preconditions.checkState;

/**
 * A source connector that generate randomized words.
 */
@Getter(AccessLevel.PACKAGE)
public class RandomConnector implements Source<byte[]> {

    private RandomConnectorConfig config;
    private Random random;

    @Override
    public void open(Map<String, Object> map,
                     SourceContext sourceContext) throws Exception {
        checkState(null == config, "Connector is already open");

        // load the configuration and validate it
        this.config = RandomConnectorConfig.load(map);
        this.config.validate();

        // create the random instance used by the connector
        this.random = new Random(null == config.getRandomSeed() ? System.currentTimeMillis() : config.getRandomSeed());
    }

    private void checkConnectorOpen() {
        checkState(null != config, "Connector is not open yet");
    }

    @Override
    public Record<byte[]> read() throws Exception {
        checkConnectorOpen();

        return new Record<byte[]>() {
            @Override
            public Optional<String> getKey() {
                return Optional.empty();
            }

            @Override
            public byte[] getValue() {
                int numBytes = random.nextInt(1024);
                byte[] data = new byte[numBytes];
                random.nextBytes(data);
                return data;
            }
        };
    }

    @Override
    public void close() throws Exception {
        // no-op
    }

}

package org.apache.pulsar.ecosystem.io.random;

import java.util.HashMap;
import java.util.Map;
import org.apache.pulsar.io.core.SourceContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Unit test {@link RandomConnector}.
 */
public class RandomConnectorTest {

    private final Map<String, Object> goodConfig = new HashMap<>();
    private final Map<String, Object> badConfig = new HashMap<>();

    @Before
    public void setup() {
        goodConfig.put("randomSeed", System.currentTimeMillis());
        goodConfig.put("maxMessageSize", 1024);
    }

    /**
     * Test opening the connector with good configuration.
     *
     * @throws Exception when fail to open the connector
     */
    @Test
    public void testOpenConnectorWithGoodConfig() throws Exception {
        RandomConnector connector = new RandomConnector();
        connector.open(goodConfig, mock(SourceContext.class));

        assertNotNull("RandomConnectorConfig should be initialized", connector.getConfig());
        assertNotNull("Random instance should be initialized", connector.getRandom());

        assertEquals(RandomConnectorConfig.load(goodConfig), connector.getConfig());
    }

    /**
     * Test opening the connector with good configuration.
     *
     * @throws Exception when fail to open the connector
     */
    @Test
    public void testOpenConnectorWithBadConfig() throws Exception {
        RandomConnector connector = new RandomConnector();
        try {
            connector.open(badConfig, mock(SourceContext.class));
            fail("Should failed to open the connector when using an invalid configuration");
        } catch (NullPointerException npe) {
            // expected
        }

        assertNotNull("RandomConnectorConfig should be initialized", connector.getConfig());
        assertNull("Random instance should not be initialized", connector.getRandom());
    }

    /**
     * Test opening the connector twice.
     *
     * @throws Exception when fail to open the connector
     */
    @Test
    public void testOpenConnectorTwice() throws Exception {
        RandomConnector connector = new RandomConnector();
        connector.open(goodConfig, mock(SourceContext.class));
        assertEquals(RandomConnectorConfig.load(goodConfig), connector.getConfig());

        Map<String, Object> anotherConfig = new HashMap<>(goodConfig);
        // change the maxMessageSize
        anotherConfig.put("maxMessageSize", 2048);
        try {
            connector.open(anotherConfig, mock(SourceContext.class));
            fail("Should fail to open a connector multiple times");
        } catch (IllegalStateException ise) {
            // expected
        }
        assertEquals(RandomConnectorConfig.load(goodConfig), connector.getConfig());
    }

    /**
     * Test opening the connector twice.
     *
     * @throws Exception when fail to open the connector
     */
    @Test
    public void testReadRecordsBeforeOpeningConnector() throws Exception {
        RandomConnector connector = new RandomConnector();
        try {
            connector.read();
            fail("Should fail to read records if a connector is not open");
        } catch (IllegalStateException ise) {
            // expected
        }
    }

}

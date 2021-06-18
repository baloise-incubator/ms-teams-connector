package com.baloise.open.ms.teams.webhook;

import com.baloise.open.ms.teams.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MessagePublisherImplTest {

  private HashMap<String, Object> defaultProperties;

  @BeforeEach
  void setUp() {
    defaultProperties = MessagePublisher.getDefaultProperties();
  }

  @Nested
  @DisplayName("Test interface defaults")
  class InterfaceDefaults {

    @Test
    @DisplayName("Verfiy DefaultProperties to be complete")
    void testDefaultProperties() {
      assertEquals(3, defaultProperties.size());
      final Set<String> keys = defaultProperties.keySet();
      assertTrue(keys.contains(MessagePublisher.PROPERTY_RETRIES));
      assertEquals(3, defaultProperties.get(MessagePublisher.PROPERTY_RETRIES));
      assertTrue(keys.contains(MessagePublisher.PROPERTY_RETRY_PAUSE));
      assertEquals(60, defaultProperties.get(MessagePublisher.PROPERTY_RETRY_PAUSE));
      assertTrue(keys.contains(MessagePublisher.PROPERTY_WEBHOOK_URI));
      assertNull(defaultProperties.get(MessagePublisher.PROPERTY_WEBHOOK_URI));
    }

    @Test
    @DisplayName("Verfiy DefaultProperties overwritten by input")
    void testDefaultPropertiesOverwritten() {
      final HashMap<String, Object> properties = new HashMap<>();
      properties.put(MessagePublisher.PROPERTY_RETRIES, 5);
      properties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, 599);
      properties.put(MessagePublisher.PROPERTY_WEBHOOK_URI, "https://my.uri.com");

      final MessagePublisherImpl instance = (MessagePublisherImpl) MessagePublisher.getInstance(properties);
      final Config config = instance.getConfig();

      assertEquals(5, config.getRetries());
      assertEquals(599000, config.getPauseBetweenRetries());
      assertEquals("https://my.uri.com", config.getWebhookURI().toString());
    }
  }

  @Nested
  @DisplayName("Test Config class properties")
  class ConfigProperyInit {

    @BeforeEach
    void setUp() {
      defaultProperties.put(MessagePublisher.PROPERTY_WEBHOOK_URI, "https://test.uri.com");
    }

    @Test
    @DisplayName("IllegalArgumentException when null is initialized")
    void testExceptionOnNull() {
      assertThrows(IllegalArgumentException.class, () -> new Config(null));
    }

    @Test
    void retries() {
      assertEquals(3, new Config(defaultProperties).getRetries());

      defaultProperties.put(MessagePublisher.PROPERTY_RETRIES, 5);
      assertEquals(5, new Config(defaultProperties).getRetries());

      defaultProperties.put(MessagePublisher.PROPERTY_RETRIES, null);
      assertEquals(1, new Config(defaultProperties).getRetries());

      defaultProperties.put(MessagePublisher.PROPERTY_RETRIES, 0);
      assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
    }

    @Test
    void pauseBetweenRetries() {
      assertEquals(60000, new Config(defaultProperties).getPauseBetweenRetries());

      defaultProperties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, 300);
      assertEquals(300000, new Config(defaultProperties).getPauseBetweenRetries());

      defaultProperties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, null);
      assertEquals(60000, new Config(defaultProperties).getPauseBetweenRetries());

      defaultProperties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, 0);
      assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
    }

    @Test
    void webhookURI() {
      assertEquals(URI.create("https://test.uri.com"), new Config(defaultProperties).getWebhookURI());

      defaultProperties.put(MessagePublisher.PROPERTY_WEBHOOK_URI, "https://github.com");
      assertEquals(URI.create("https://github.com"), new Config(defaultProperties).getWebhookURI());

      defaultProperties.put(MessagePublisher.PROPERTY_WEBHOOK_URI, null);
      assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));

      defaultProperties.put(MessagePublisher.PROPERTY_WEBHOOK_URI, "everything but no URI");
      assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
    }
  }
}
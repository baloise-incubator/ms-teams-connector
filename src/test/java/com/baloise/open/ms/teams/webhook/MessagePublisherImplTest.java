package com.baloise.open.ms.teams.webhook;

import com.baloise.open.ms.teams.Config;
import com.baloise.open.ms.teams.templates.MessageCard;
import com.google.gson.GsonBuilder;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.verify.VerificationTimes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.org.webcompere.systemstubs.SystemStubs.withEnvironmentVariable;

class MessagePublisherImplTest {

  private Map<String, Object> defaultProperties;

  @BeforeEach
  void setUp() {
    defaultProperties = MessagePublisher.getDefaultProperties();
  }

  @Nested
  @DisplayName("Test interface defaults")
  class InterfaceDefaultsTest {

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
    @DisplayName("Verify DefaultProperties overwritten by input")
    void testDefaultPropertiesOverwritten() {
      final Map<String, Object> properties = new HashMap<>();
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
  class ConfigPropertyInitTest {

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
      assertAll(
          () -> assertEquals(3, new Config(defaultProperties).getRetries()),
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_RETRIES, 5);
            assertEquals(5, new Config(defaultProperties).getRetries());
          },
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_RETRIES, null);
            assertEquals(1, new Config(defaultProperties).getRetries());
          },
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_RETRIES, 0);
            assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
          },
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_RETRIES, "aString");
            assertEquals(1, new Config(defaultProperties).getRetries());
          },
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_RETRIES, "22");
            assertEquals(22, new Config(defaultProperties).getRetries());
          }
      );
    }

    @Test
    void pauseBetweenRetries() {
      assertAll(
          () -> assertEquals(60000, new Config(defaultProperties).getPauseBetweenRetries()),
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, 300);
            assertEquals(300000, new Config(defaultProperties).getPauseBetweenRetries());
          },
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, null);
            assertEquals(60000, new Config(defaultProperties).getPauseBetweenRetries());
          },
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, 0);
            assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
          },
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, "aString");
            assertEquals(60000, new Config(defaultProperties).getPauseBetweenRetries());
          },
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, "22");
            assertEquals(22000, new Config(defaultProperties).getPauseBetweenRetries());
          }
      );
    }

    @Test
    void webhookURI() {
      assertAll(
          () -> assertEquals(URI.create("https://test.uri.com"), new Config(defaultProperties).getWebhookURI()),
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_WEBHOOK_URI, "https://github.com");
            assertEquals(URI.create("https://github.com"), new Config(defaultProperties).getWebhookURI());
          },
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_WEBHOOK_URI, null);
            assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
          },
          () -> {
            defaultProperties.put(MessagePublisher.PROPERTY_WEBHOOK_URI, "everything but no URI");
            assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
          }
      );
    }
  }

  @Nested
  @DisplayName("Test webhook MessagePublisher")
  @ExtendWith(MockServerExtension.class)
  class MessagePublisherTest {

    private final String testMessage = "{\"title\":\"UnitTest\",\"content\":\"I should be some JSON content\"}";

    private Map<String, Object> getTestProperties() {
      final Map<String, Object> testProperties = MessagePublisher.getDefaultProperties();
      testProperties.put(MessagePublisher.PROPERTY_WEBHOOK_URI, "https://test.webhook.com/");
      testProperties.put(MessagePublisher.PROPERTY_RETRIES, 1);
      testProperties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, 1);
      return testProperties;
    }

    @Test
    @DisplayName("HttpEntity is created applying text, contentType and encoding")
    void testStringPublishing() {
      ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
      final HttpPost httpPostMock = Mockito.mock(HttpPost.class);
      Mockito.doNothing().when(httpPostMock).setEntity(httpEntityCaptor.capture());

      final MessagePublisherImpl testee = new MessagePublisherImpl(getTestProperties());
      ScheduledFuture<?> publishedFuture = testee.scheduleMessagePublishing(testMessage, httpPostMock);
      assertNotNull(publishedFuture);

      final HttpEntity entity = httpEntityCaptor.getValue();
      assertAll(
          () -> assertNotNull(entity),
          () -> assertEquals(ContentType.APPLICATION_JSON.toString(), entity.getContentType()),
          () -> assertEquals(StandardCharsets.UTF_8.toString(), entity.getContentEncoding()),
          () -> {
            try (final InputStreamReader in = new InputStreamReader(entity.getContent());
                 final BufferedReader bufferedReader = new BufferedReader(in)) {
              assertEquals(testMessage, bufferedReader.readLine());
            }
          }
      );
    }

    @Test
    @DisplayName("HttpEntity is created applying content from MessageCard, contentType and encoding")
    void testMessagCardPublishing(MockServerClient client) {
      final MessagePublisherImpl testee = (MessagePublisherImpl) MessagePublisher.getInstance(getExtractedUri(client));
      final MessageCard messageCard = new MessageCard("MyTitle", "MySummary");

      ScheduledFuture<?> publishedFuture = testee.publish(messageCard);
      assertNotNull(publishedFuture);

      final HttpRequest mockedPost = HttpRequest.request()
          .withMethod("POST")
          .withContentType(MediaType.JSON_UTF_8)
          .withBody(new GsonBuilder().create().toJson(messageCard));

      client.verify(mockedPost, VerificationTimes.exactly(1));
      assertTrue(testee.getConfig().getRetries() > 1);
      client.reset();
    }

    @Test
    @DisplayName("POST is executed inside EXECUTOR_SERVICE matching request at first try")
    void testPostHappyCase(MockServerClient client) {
      final MessagePublisherImpl testee = (MessagePublisherImpl) MessagePublisher.getInstance(getExtractedUri(client));
      ScheduledFuture<?> publishedFuture = testee.publish(testMessage);
      assertNotNull(publishedFuture);

      final HttpRequest mockedPost = HttpRequest.request()
          .withMethod("POST")
          .withContentType(MediaType.JSON_UTF_8)
          .withBody(testMessage);

      client.verify(mockedPost, VerificationTimes.exactly(1));
      assertTrue(testee.getConfig().getRetries() > 1);
      client.reset();
    }

    @ParameterizedTest
    @ValueSource(strings = {"https_proxy","HTTPS_PROXY", "ANY_other_sys_env_but_not_proxy"})
    void testHttpClientUsesProxyWhenSetLowerCase(String sysEnvParamName, MockServerClient client) throws Exception {
      final String proxyUrl = getExtractedUri(client);
      withEnvironmentVariable(sysEnvParamName, proxyUrl).execute(() -> {
        final HttpRequest mockedPost = HttpRequest.request().withMethod("POST");
        client.when(mockedPost)
            .respond(
                HttpResponse.response().withBody("{}").withStatusCode(HttpStatus.SC_OK)
            );

        final String enpointUrl = getExtractedUriButWrongPort(client);
        final MessagePublisherImpl testee = (MessagePublisherImpl) MessagePublisher.getInstance(enpointUrl + "/MYENDPOINT");
        ScheduledFuture<?> publishedFuture = testee.publish(testMessage);
        assertNotNull(publishedFuture);
        Awaitility.await()
            .atMost(Durations.TWO_SECONDS /* wait for executor service */)
            .untilAsserted(() ->
                client.verify(mockedPost
                        .withBody(testMessage)
                        .withContentType(MediaType.JSON_UTF_8),
                    VerificationTimes.exactly(
                        "https_proxy".equalsIgnoreCase(sysEnvParamName)
                            ? 1 /* call succeeded via proxy */
                            : 0 /* call failed since wrong port */ )));

        assertTrue(testee.getConfig().getRetries() > 1);
        assertNotEquals(proxyUrl, enpointUrl);
        client.reset();
      });
    }

    @Test
    @DisplayName("POST is executed 2 times during failure")
    void test2RetriesInCaseOfFailure(MockServerClient client) {
      testRetrials(client, 1);
    }

    @Test
    @DisplayName("POST is executed 3 times during failure")
    void test3RetriesInCaseOfFailure(MockServerClient client) {
      testRetrials(client, 2);
    }

    private void testRetrials(MockServerClient client, int numberOf504Replies) {
      ConfigurationProperties.maxSocketTimeout(5000);
      final HttpRequest mockedPost = HttpRequest.request().withMethod("POST");
      client.when(
          // mock first n calls replying with 504
          mockedPost, Times.exactly(numberOf504Replies)
      ).respond(
          HttpResponse.response().withBody("{}").withStatusCode(HttpStatus.SC_GATEWAY_TIMEOUT)
      );
      client.when(
          // n+1 call shall be successful
          mockedPost, Times.exactly(numberOf504Replies + 1)
      ).respond(
          HttpResponse.response().withBody("{}").withStatusCode(HttpStatus.SC_OK)
      );

      final Map<String, Object> properties = MessagePublisher.getDefaultProperties();
      final String expectedUri = getExtractedUri(client);
      properties.put(MessagePublisher.PROPERTY_WEBHOOK_URI, expectedUri);
      properties.put(MessagePublisher.PROPERTY_RETRY_PAUSE, 1); // speed up the test
      final MessagePublisherImpl testee = (MessagePublisherImpl) MessagePublisher.getInstance(properties);

      ScheduledFuture<?> publishedFuture = testee.publish(testMessage);
      assertNotNull(publishedFuture);

      assertEquals(3, testee.getConfig().getRetries());
      assertEquals(expectedUri, testee.getConfig().getWebhookURI().toString());

      Awaitility.await()
          .atMost(Durations.FIVE_SECONDS /* wait for executor service */)
          .untilAsserted(() ->
              client.verify(mockedPost.withBody(testMessage)
                      .withContentType(MediaType.JSON_UTF_8),
                  VerificationTimes.exactly(numberOf504Replies + 1)));
      client.reset();
    }

    private String getExtractedUri(MockServerClient client) {
      return "http://%s:%d".formatted(
          client.remoteAddress().getAddress().getHostAddress(),
          client.remoteAddress().getPort());
    }

    private String getExtractedUriButWrongPort(MockServerClient client) {
      return "http://%s:%d".formatted(
          client.remoteAddress().getAddress().getHostAddress(),
          client.remoteAddress().getPort()-1);
    }
  }
}

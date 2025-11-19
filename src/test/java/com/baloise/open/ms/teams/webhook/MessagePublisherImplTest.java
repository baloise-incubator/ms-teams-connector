package com.baloise.open.ms.teams.webhook;

import com.baloise.open.ms.teams.Config;
import com.baloise.open.ms.teams.json.Serializer;
import com.baloise.open.ms.teams.templates.AdaptiveCard;
import com.baloise.open.ms.teams.templates.AdaptiveCardFactory;
import com.baloise.open.ms.teams.templates.Badge;
import com.baloise.open.ms.teams.templates.TeamsMessage;
import com.baloise.open.ms.teams.templates.TeamsMessageFactory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.org.webcompere.systemstubs.SystemStubs.withEnvironmentVariable;

class MessagePublisherImplTest {

    private Map<String, Object> defaultProperties;
    private final String webhookURI = "https://my.uri.com";

    @BeforeEach
    void setUp() {
        defaultProperties = new HashMap<>();
        defaultProperties.put(Config.PROPERTY_WEBHOOK_URI, webhookURI);
    }

    @Nested
    @DisplayName("Test interface defaults")
    class InterfaceDefaultsTest {
        @Test
        @DisplayName("Verfiy DefaultProperties to be complete")
        void testDefaultProperties() {
            Config config = new Config(defaultProperties);

            assertEquals(Config.DEFAULT_RETRIES, config.getRetries());
            assertEquals(Config.DEFAULT_PAUSE_BETWEEN_RETRIES, config.getPauseBetweenRetries());
            assertEquals(webhookURI, config.getWebhookURI().toString());
            assertNull(config.getProxyURI());
            assertFalse(config.isBlocking());
        }

        @Test
        @DisplayName("Verfiy DefaultProperties to be complete")
        void testDefaultBuilder() {
            Config config = Config.builder()
                    .withWebhookURI(webhookURI)
                    .build();

            assertEquals(Config.DEFAULT_RETRIES, config.getRetries());
            assertEquals(Config.DEFAULT_PAUSE_BETWEEN_RETRIES, config.getPauseBetweenRetries());
            assertEquals(webhookURI, config.getWebhookURI().toString());
            assertNull(config.getProxyURI());
            assertFalse(config.isBlocking());
        }

        @Test
        @DisplayName("Verify DefaultProperties overwritten by input")
        void testDefaultPropertiesOverwritten() {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(Config.PROPERTY_RETRIES, 5);
            properties.put(Config.PROPERTY_RETRY_PAUSE, 599);
            properties.put(Config.PROPERTY_WEBHOOK_URI, "https://my.uri.com");
            properties.put(Config.PROPERTY_PROXY_URI, "https://proxy.uri.com");
            properties.put(Config.PROPERTY_BLOCKING, Boolean.TRUE);

            final MessagePublisherImpl instance = (MessagePublisherImpl) MessagePublisher.getInstance(properties);
            final Config config = instance.getConfig();

            assertEquals(5, config.getRetries());
            assertEquals(599000, config.getPauseBetweenRetries());
            assertNotNull(config.getWebhookURI());
            assertEquals("https://my.uri.com", config.getWebhookURI().toString());
            assertNotNull(config.getProxyURI());
            assertEquals("https://proxy.uri.com", config.getProxyURI().toString());
            assertTrue(config.isBlocking());
        }
    }

    @Nested
    @DisplayName("Test Config class properties")
    class ConfigPropertyInitTest {

        @BeforeEach
        void setUp() {
            defaultProperties.put(Config.PROPERTY_WEBHOOK_URI, "https://test.uri.com");
            defaultProperties.put(Config.PROPERTY_PROXY_URI, "https://proxy.uri.com");
        }

        @Test
        @DisplayName("IllegalArgumentException when null is initialized")
        void testExceptionOnNull() {
            assertThrows(IllegalArgumentException.class, () -> new Config(null));
            Config.Builder builder = Config.builder();
            assertThrows(IllegalArgumentException.class, builder::build);
        }

        @Test
        void retries() {
            assertAll(
                    () -> assertEquals(Config.DEFAULT_RETRIES, new Config(defaultProperties).getRetries()),
                    () -> assertEquals(Config.DEFAULT_RETRIES, Config.builder().withWebhookURI(webhookURI).build().getRetries()),
                    () -> {
                        defaultProperties.put(Config.PROPERTY_RETRIES, 5);
                        assertEquals(5, new Config(defaultProperties).getRetries());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_RETRIES, null);
                        assertEquals(Config.DEFAULT_RETRIES, new Config(defaultProperties).getRetries());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_RETRIES, 0);
                        assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_RETRIES, "aString");
                        assertEquals(Config.DEFAULT_RETRIES, new Config(defaultProperties).getRetries());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_RETRIES, "22");
                        assertEquals(22, new Config(defaultProperties).getRetries());
                    }
            );
        }

        @Test
        void pauseBetweenRetries() {
            assertAll(
                    () -> assertEquals(60000, new Config(defaultProperties).getPauseBetweenRetries()),
                    () -> assertEquals(60000, Config.builder().withWebhookURI(webhookURI).build().getPauseBetweenRetries()),
                    () -> {
                        defaultProperties.put(Config.PROPERTY_RETRY_PAUSE, 300);
                        assertEquals(300000, new Config(defaultProperties).getPauseBetweenRetries());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_RETRY_PAUSE, null);
                        assertEquals(60000, new Config(defaultProperties).getPauseBetweenRetries());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_RETRY_PAUSE, 0);
                        assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_RETRY_PAUSE, "aString");
                        assertEquals(60000, new Config(defaultProperties).getPauseBetweenRetries());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_RETRY_PAUSE, "22");
                        assertEquals(22000, new Config(defaultProperties).getPauseBetweenRetries());
                    }
            );
        }

        @Test
        void webhookURI() {
            assertAll(
                    () -> assertEquals(URI.create("https://test.uri.com"), new Config(defaultProperties).getWebhookURI()),
                    () -> assertEquals(URI.create(webhookURI), Config.builder().withWebhookURI(webhookURI).build().getWebhookURI()),
                    () -> {
                        defaultProperties.put(Config.PROPERTY_WEBHOOK_URI, "https://github.com");
                        assertEquals(URI.create("https://github.com"), new Config(defaultProperties).getWebhookURI());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_WEBHOOK_URI, null);
                        assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_WEBHOOK_URI, "everything but no URI");
                        assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
                    }
            );
        }

        @Test
        void proxyURI() {
            assertAll(
                    () -> assertEquals(URI.create("https://proxy.uri.com"), new Config(defaultProperties).getProxyURI()),
                    () -> assertEquals(URI.create("https://proxy.uri.com"), Config.builder().withWebhookURI(webhookURI).withProxyURI("https://proxy.uri.com").build().getProxyURI()),
                    () -> {
                        defaultProperties.put(Config.PROPERTY_PROXY_URI, "https://github.com");
                        assertEquals(URI.create("https://github.com"), new Config(defaultProperties).getProxyURI());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_PROXY_URI, null);
                        assertNull(new Config(defaultProperties).getProxyURI());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_PROXY_URI, "everything but no URI");
                        assertThrows(IllegalArgumentException.class, () -> new Config(defaultProperties));
                    }
            );
        }

        @Test
        void blocking() {
            assertAll(
                    () -> assertFalse(new Config(defaultProperties).isBlocking()),
                    () -> assertFalse(Config.builder().withWebhookURI(webhookURI).build().isBlocking()),
                    () -> {
                        defaultProperties.put(Config.PROPERTY_BLOCKING, Boolean.TRUE.toString());
                        assertTrue(new Config(defaultProperties).isBlocking());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_BLOCKING, null);
                        assertFalse(new Config(defaultProperties).isBlocking());
                    },
                    () -> {
                        defaultProperties.put(Config.PROPERTY_BLOCKING, "everything but no URI");
                        assertFalse(new Config(defaultProperties).isBlocking());
                    }
            );
        }

        @Test
        void verifyToString() {
            final Config testee = Config.builder()
                .withWebhookURI("https://github.com")
                .withProxyURI("https://proxy.uri.com")
                .withRetries(65)
                .withPauseBetweenRetries(37)
                .withBlocking(true).build();

            assertEquals("Config(retries=65, pauseBetweenRetries=37000, webhookURI=https://github.com, proxyURI=https://proxy.uri.com, blocking=true)",
                testee.toString());
        }
    }

    @Nested
    @DisplayName("Test webhook MessagePublisher")
    @ExtendWith(MockServerExtension.class)
    class MessagePublisherTest {

        private final String testMessage = "{\"title\":\"UnitTest\",\"content\":\"I should be some JSON content\"}";

        private Config getTestProperties() {
            return Config.builder()
                    .withWebhookURI("https://test.webhook.com/")
                    .withProxyURI("https://proxy.webhook.com/")
                    .withRetries(1)
                    .withPauseBetweenRetries(1)
                    .build();
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
                    () -> {
                        try (final InputStreamReader in = new InputStreamReader(entity.getContent());
                             final BufferedReader bufferedReader = new BufferedReader(in)) {
                            assertEquals(testMessage, bufferedReader.readLine());
                        }
                    }
            );
        }

        @Test
        @DisplayName("HttpEntity is created applying text, contentType and encoding")
        void testGetInstanceWithProxy() {

            String proxyUrl = "localhost:8080";
            String webhookUrl = "https://proxy.webhook.com/";
            MessagePublisherImpl publisher = (MessagePublisherImpl) MessagePublisher.getInstance(proxyUrl, webhookUrl);

            Config config = publisher.getConfig();

            assertNotNull(config);
            assertNotNull(config.getProxyURI());
            assertEquals(proxyUrl, config.getProxyURI().toString());
            assertEquals(webhookUrl, config.getWebhookURI().toString());
            assertEquals(Config.DEFAULT_RETRIES, config.getRetries());
            assertEquals(Config.DEFAULT_PAUSE_BETWEEN_RETRIES, config.getPauseBetweenRetries());
            assertEquals(Config.DEFAULT_BLOCKING, config.isBlocking());
        }

        @Test
        @DisplayName("HttpEntity is created applying content from Adaptive, contentType")
        void testMessageCardPublishing(MockServerClient client) {
            final MessagePublisherImpl testee = (MessagePublisherImpl) MessagePublisher.getInstance(getExtractedUri(client));
            final AdaptiveCard adaptiveCard = AdaptiveCard.builder().body(List.of(Badge.builder().build())).build();

            ScheduledFuture<?> publishedFuture = testee.publish(adaptiveCard);
            assertNotNull(publishedFuture);

            final HttpRequest mockedPost = HttpRequest.request()
                    .withMethod("POST")
                    .withContentType(MediaType.JSON_UTF_8)
                    .withBody(Serializer.asJson(adaptiveCard));

            Awaitility.await()
              .atMost(Durations.FIVE_SECONDS)
              .untilAsserted(() -> client.verify(mockedPost, VerificationTimes.exactly(1)));
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

            Awaitility.await()
              .atMost(Durations.FIVE_SECONDS)
              .untilAsserted(() -> client.verify(mockedPost, VerificationTimes.exactly(1)));
            assertTrue(testee.getConfig().getRetries() > 1);
            client.reset();
        }

        @Test
        @DisplayName("publish(TeamsMessage) sends JSON to webhook")
        void testPublishTeamsMessage(MockServerClient client) {
            final MessagePublisherImpl testee = (MessagePublisherImpl) MessagePublisher.getInstance(getExtractedUri(client));
            TeamsMessage message = TeamsMessageFactory.createTeamsMessageWithAdaptiveCard(
                    "SummaryTest",
                    AdaptiveCardFactory.createSimpleAdaptiveCard("TitleTest", "BodyTest")
            );

            ScheduledFuture<?> future = testee.publish(message); // nutzt default publish(TeamsMessage)
            assertNotNull(future);

            final HttpRequest expectedPost = HttpRequest.request()
                    .withMethod("POST")
                    .withContentType(MediaType.JSON_UTF_8)
                    .withBody(Serializer.asJson(message));

            Awaitility.await()
                    .atMost(Durations.FIVE_SECONDS)
                    .untilAsserted(() -> client.verify(expectedPost, VerificationTimes.exactly(1)));

            assertTrue(testee.getConfig().getRetries() > 1); // konsistent mit anderen Tests
            client.reset();
        }

        @ParameterizedTest
        @ValueSource(strings = {"https_proxy", "HTTPS_PROXY", "ANY_other_sys_env_but_not_proxy"})
        void testHttpClientUsesProxyWhenSetLowerCase(String sysEnvParamName, MockServerClient client) throws Exception {
            final String proxyUrl = getExtractedUri(client);
            withEnvironmentVariable(sysEnvParamName, proxyUrl).execute(() -> {
                final HttpRequest mockedPost = HttpRequest.request().withMethod("POST");
                client.when(mockedPost)
                        .respond(
                                HttpResponse.response().withBody("{}").withStatusCode(HttpStatus.SC_OK)
                        );

                final String endpointUrl = getExtractedUriButWrongPort(client);
                final MessagePublisherImpl testee = (MessagePublisherImpl) MessagePublisher.getInstance(endpointUrl + "/MYENDPOINT");
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
                                                        : 0 /* call failed since wrong port */)));

                assertTrue(testee.getConfig().getRetries() > 1);
                assertNotEquals(proxyUrl, endpointUrl);
                client.reset();
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {0,1,2 /* lower max*/,3 /* exact max retries*/ ,5 /* more than max should default to max*/})
        @DisplayName("Publish behaves blocking when configured so")
        void testBlockingint(int numberOf504Replies, MockServerClient client) {
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

            final String expectedUri = getExtractedUri(client);
            final int maxRetries = 3;
            final Config config = Config.builder()
                    .withWebhookURI(expectedUri)
                    .withRetries(maxRetries)
                    .withPauseBetweenRetries(2)
                    .withBlocking(true)
                    .build();
            final MessagePublisherImpl testee = (MessagePublisherImpl) MessagePublisher.getInstance(config);

            ScheduledFuture<?> publishedFuture = testee.publish(testMessage);
            assertNotNull(publishedFuture);
            assertTrue(publishedFuture.isDone());

            assertEquals(maxRetries, testee.getConfig().getRetries());
            assertEquals(expectedUri, testee.getConfig().getWebhookURI().toString());

            final int expected = (numberOf504Replies >= maxRetries - 1)
                ? maxRetries
                : numberOf504Replies + 1;

            client.verify(mockedPost, VerificationTimes.exactly(expected));
            assertTrue(testee.getConfig().getRetries() > 1);
            client.reset();
        }

        @ParameterizedTest
        @ValueSource(ints = {0,1,2 /* lower max*/,3 /* exact max retries*/ ,5 /* more than max should default to max*/})
        @DisplayName("POST is executed n times during failure")
        void testRetriesInCaseOfFailure(int numberOf504Replies, MockServerClient client) {
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

            final String expectedUri = getExtractedUri(client);
            final int maxRetries = 3;
            final Config config = Config.builder()
                    .withWebhookURI(expectedUri)
                    .withRetries(maxRetries)
                    .withPauseBetweenRetries(1)
                    .build();
            final MessagePublisherImpl testee = (MessagePublisherImpl) MessagePublisher.getInstance(config);

            ScheduledFuture<?> publishedFuture = testee.publish(testMessage);
            assertNotNull(publishedFuture);

            assertEquals(maxRetries, testee.getConfig().getRetries());
            assertEquals(expectedUri, testee.getConfig().getWebhookURI().toString());

            final int expected = (numberOf504Replies >= maxRetries - 1)
                ? maxRetries
                : numberOf504Replies + 1;
            Awaitility.await()
                    .atMost(Durations.FIVE_SECONDS /* wait for executor service */)
                    .untilAsserted(() ->
                            client.verify(mockedPost.withBody(testMessage)
                                            .withContentType(MediaType.JSON_UTF_8),
                                    VerificationTimes.exactly(expected)));
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
                    client.remoteAddress().getPort() - 1);
        }
    }
}

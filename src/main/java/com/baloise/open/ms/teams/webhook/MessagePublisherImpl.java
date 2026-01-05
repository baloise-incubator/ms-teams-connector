/*
 * Copyright 2021 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baloise.open.ms.teams.webhook;

import com.baloise.open.ms.teams.Config;
import com.baloise.open.ms.teams.json.SerializableMessage;
import com.baloise.open.ms.teams.json.Serializer;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;

@Slf4j
class MessagePublisherImpl implements MessagePublisher {

    private final Config config;
    final HttpPost httpPost;

    MessagePublisherImpl(final Config config) {
        if (log.isDebugEnabled()) {
            log.debug("Creating MessagePublisherImpl with config: {}", config);
        }
        this.config = config;
        httpPost = new HttpPost(this.config.getWebhookURI());
    }

    @Override
    public ScheduledFuture<?> publish(final SerializableMessage message) {
        return scheduleMessagePublishing(Serializer.asJson(message), httpPost);
    }

    @Override
    public ScheduledFuture<?> publish(String jsonBody) {
        return scheduleMessagePublishing(jsonBody, httpPost);
    }

    ScheduledFuture<?> scheduleMessagePublishing(String jsonBody, HttpPost httpPost) {
        httpPost.setEntity(EntityBuilder.create()
                .setText(jsonBody)
                .setContentType(ContentType.APPLICATION_JSON)
                .build());

        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        final HttpClientPostExecutor httpClientPostExec = new HttpClientPostExecutor(httpPost, executor);
        ScheduledFuture<?> scheduledFuture = executor.scheduleWithFixedDelay(
                httpClientPostExec,
                0 /* no delay */,
                config.getPauseBetweenRetries(),
                TimeUnit.MILLISECONDS);

        if (config.isBlocking()) {
            awaitTermination(executor);
        }
        return scheduledFuture;
    }

    private void awaitTermination(final ScheduledExecutorService executor) {
        long terminationTimeout = (config.getRetries()) * config.getPauseBetweenRetries();
        try {
            log.debug("Blocking mode is enabled, waiting for max {}ms for the webhook to return a response.", terminationTimeout);
            if (!executor.awaitTermination(terminationTimeout, TimeUnit.MILLISECONDS)) {
                log.warn("Webhook did not return a response within the timeout period of {} seconds.", terminationTimeout);
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException: ", e);
            Thread.currentThread().interrupt();
        } finally {
            log.debug("continue");
        }
    }

    Config getConfig() {
        return config;
    }

    @Override
    public CloseableHttpClient getHttpClient() {
        return Optional.ofNullable(config.getProxyURI())
                .or(() -> Stream.of(System.getenv("https_proxy"), System.getenv("HTTPS_PROXY"))
                        .filter(StringUtils::isNotBlank)
                        .findFirst()
                        .map(URI::create)
                ).map(proxy -> {
                            if (log.isDebugEnabled()) {
                                log.debug("Using proxy: {}", proxy);
                            }
                            return HttpClients.custom()
                                    .setProxy(new HttpHost(proxy.getHost(), proxy.getPort()))
                                    .build();
                        }
                ).orElseGet(HttpClients::createDefault);
    }

    private final class HttpClientPostExecutor implements Runnable {

        final ScheduledExecutorService scheduledExecutorService;
        final HttpPost httpPost;
        final AtomicInteger execCounter = new AtomicInteger(0);

        HttpClientPostExecutor(final HttpPost httpPost, ScheduledExecutorService executor) {
            this.scheduledExecutorService = executor;
            this.httpPost = httpPost;
        }

        @Override
        public void run() {
            try (final CloseableHttpClient httpclient = getHttpClient()) {
                httpclient.execute(httpPost, response -> {
                    final int responseCode = response.getCode();

                    final HttpEntity entity = response.getEntity();
                    final String body = EntityUtils.toString(entity);
                    EntityUtils.consume(entity);

                    if (HttpStatus.SC_OK == responseCode || HttpStatus.SC_ACCEPTED == responseCode) {
                        log.debug("Webhook {} return with HttpStatus 200.", config.getWebhookURI());
                        cancel();
                        return responseCode;
                    }

                    log.debug(body);
                    throw new IllegalStateException(
                            "Posting data to %s may have failed. Webhook responded with status code %s"
                                    .formatted(config.getWebhookURI(), responseCode));
                });

            } catch (Exception e) {
                log.warn(e.getMessage());

                if (config.getRetries() == execCounter.incrementAndGet()) {
                    log.warn("Giving up after {} attempts.", config.getRetries());
                    cancel();
                } else {
                    log.info("Retry in {} seconds", config.getPauseBetweenRetries() / 1000);
                }
            }
        }

        private void cancel() {
            log.debug("Shutdown scheduled executor service");
            scheduledExecutorService.shutdown();
        }
    }

    @Override
    public void publishSync(final SerializableMessage message) {
        publishSync(Serializer.asJson(message));
    }

    @Override
    public void publishSync(final String jsonBody) {
        final AtomicInteger attemptCounter = new AtomicInteger(0);

        while (attemptCounter.get() < config.getRetries()) {
            try (final CloseableHttpClient httpClient = getHttpClient()) {
                httpPost.setEntity(EntityBuilder.create()
                        .setText(jsonBody)
                        .setContentType(ContentType.APPLICATION_JSON)
                        .build());

                httpClient.execute(httpPost, response -> {
                    final int responseCode = response.getCode();

                    if (HttpStatus.SC_OK == responseCode || HttpStatus.SC_ACCEPTED == responseCode) {
                        log.debug("Webhook {} returned with HttpStatus 200.", config.getWebhookURI());
                        return null;
                    } else {
                        throw new MessagePublishingException(
                                "Posting data to %s may have failed. Webhook responded with status code %s"
                                        .formatted(config.getWebhookURI(), responseCode));
                    }
                });
                return; // Exit the method if successful
            } catch (MessagePublishingException e) {
                log.warn(e.getMessage());
                if (attemptCounter.incrementAndGet() >= config.getRetries()) {
                    log.warn("Giving up after {} attempts.", config.getRetries());
                    throw e;
                } else {
                    log.info("Retrying in {} milliseconds", config.getPauseBetweenRetries());
                    try {
                        TimeUnit.MILLISECONDS.sleep(config.getPauseBetweenRetries());
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                        throw new MessagePublishingException("Thread was interrupted", interruptedException);
                    }
                }
            } catch (Exception e) {
                log.warn(e.getMessage());
                throw new MessagePublishingException("Unexpected error occurred while publishing message", e);
            }
        }
    }
}
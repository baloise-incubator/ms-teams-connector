/*
 * Copyright 2021 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baloise.open.ms.teams.webhook;

import com.baloise.open.ms.teams.Config;
import com.baloise.open.ms.teams.templates.MessageCard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
class MessagePublisherImpl implements MessagePublisher {

  private final Config config;
  final HttpPost httpPost;

  MessagePublisherImpl(final Map<String, Object> properties) {
    log.debug(properties.toString());
    config = new Config(properties);
    httpPost = new HttpPost(config.getWebhookURI());
  }

  @Override
  public ScheduledFuture<?> publish(final MessageCard messageCard) {
    final Gson gson = new GsonBuilder().create();
    return scheduleMessagePublishing(gson.toJson(messageCard), httpPost);
  }

  @Override
  public ScheduledFuture<?> publish(String jsonBody) {
    return scheduleMessagePublishing(jsonBody, httpPost);
  }

  ScheduledFuture<?> scheduleMessagePublishing(String jsonBody, HttpPost httpPost) {
    httpPost.setEntity(EntityBuilder.create()
        .setText(jsonBody)
        .setContentType(ContentType.APPLICATION_JSON)
        .setContentEncoding(StandardCharsets.UTF_8.name()).build());

    final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    final HttpClientPostExecutor httpClientPostExec = new HttpClientPostExecutor(httpPost, executor);
    return executor.scheduleWithFixedDelay(httpClientPostExec, 0, config.getPauseBetweenRetries(), TimeUnit.MILLISECONDS);
  }

  Config getConfig() {
    return config;
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
      try (final CloseableHttpClient httpclient = HttpClients.createDefault();
           final CloseableHttpResponse response = httpclient.execute(httpPost)) {

        final HttpEntity entity = response.getEntity();
        final String body = EntityUtils.toString(entity);
        final int responseCode = response.getStatusLine().getStatusCode();
        EntityUtils.consume(entity);

        if (HttpStatus.SC_OK == responseCode) {
          log.debug("Webhook {} return with HttpStatus 200.", config.getWebhookURI());
          cancel();
          return;
        }

        log.debug(body);
        throw new IllegalStateException(String.format("Posting data to %s may have failed. Webhook responded with status code %s",
            config.getWebhookURI(), responseCode));

      } catch (Exception e) {
        log.warn(e.getMessage());

        if (config.getRetries() == execCounter.incrementAndGet()) {
          log.warn(String.format("Giving up after %d attempts.", config.getRetries()));
          cancel();
        } else {
          log.info(String.format("Retry in %d seconds", config.getPauseBetweenRetries() / 1000));
        }
      }
    }

    private void cancel() {
      log.debug("Shutdown scheduled executor service");
      scheduledExecutorService.shutdown();
    }
  }
}

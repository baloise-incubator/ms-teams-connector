/*
 * Copyright 2018 - 2021 Baloise Group
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
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MessagePublisherImpl implements MessagePublisher {

  private static final Logger LOG = LogManager.getLogger(MessagePublisherImpl.class);
  static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

  private final Config config;

  MessagePublisherImpl(final Map<String, Object> properties) {
    LOG.info(properties);
    config = new Config(properties);
  }

  @Override
  public void publish(final MessageCard messageCard) {
    final HttpPost httpPost = new HttpPost(config.getWebhookURI());
    final String postBody = new GsonBuilder().create().toJson(messageCard);

    httpPost.setEntity(EntityBuilder.create()
                           .setText(postBody)
                           .setContentType(ContentType.APPLICATION_JSON)
                           .setContentEncoding(StandardCharsets.UTF_8.name()).build());

    EXECUTOR_SERVICE.scheduleWithFixedDelay(new HttpClientPostExecutor(httpPost),
        0,
        config.getPauseBetweenRetries(),
        TimeUnit.MILLISECONDS);
  }

  public Config getConfig() {
    return config;
  }

  private class HttpClientPostExecutor implements Runnable {

    final HttpPost httpPost;
    final AtomicInteger execCounter = new AtomicInteger(0);

    HttpClientPostExecutor(final HttpPost httpPost) {
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
          EXECUTOR_SERVICE.shutdown();
          return;
        }

        LOG.warn(String.format("Posting data to %s may have failed. Webhook responded with status code %s", config.getWebhookURI(), responseCode));
        LOG.debug(body);

        if (config.getRetries() == execCounter.incrementAndGet()) {
          LOG.warn(String.format("Giving up after %d attempts.", config.getRetries()));
          EXECUTOR_SERVICE.shutdown();
        } else {
          LOG.info(String.format("Retry in %d seconds", config.getPauseBetweenRetries() / 1000));
        }

      } catch (Exception e) {
        LOG.error("Failed to post data to webhook " + config.getWebhookURI(), e);
        EXECUTOR_SERVICE.shutdown();
      }
    }
  }
}

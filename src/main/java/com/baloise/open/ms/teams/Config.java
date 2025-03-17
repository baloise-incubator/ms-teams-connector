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
package com.baloise.open.ms.teams;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.Map;

@Slf4j
@Getter
public final class Config {

  /**
   * defines the number of retires in case of technical interruptions
   */
  public static final String PROPERTY_RETRIES = "com.baloise.open.ms.teams.retries";

  /**
   * defines the pause time between {@link #PROPERTY_RETRIES} in seconds
   */
  public static final String PROPERTY_RETRY_PAUSE = "com.baloise.open.ms.teams.retries.pause";

  /**
   * defines the webhooks URI
   */
  public static final String PROPERTY_WEBHOOK_URI = "com.baloise.open.ms.teams.webhook.uri";

  private final int retries;
  private final long pauseBetweenRetries;
  private final URI webhookURI;

  public Config(final Map<String, Object> properties) {
    retries = setRetries(properties);
    pauseBetweenRetries = setPauseBetweenRetries(properties);
    webhookURI = setWebhookURI(properties);
  }

  private URI setWebhookURI(Map<String, Object> properties) {
    if (properties == null || properties.get(PROPERTY_WEBHOOK_URI) == null) {
      throw new IllegalArgumentException(String.format("Parameter %s must not be null.", PROPERTY_WEBHOOK_URI));
    }

    final String stringValue = String.valueOf(properties.get(PROPERTY_WEBHOOK_URI));
    if (StringUtils.isBlank(stringValue)) {
      throw new IllegalArgumentException(String.format("Parameter %s must not be blank.", PROPERTY_WEBHOOK_URI));
    }
    try {
      return URI.create(stringValue);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(String.format("Parameter %s must be a valid URI (%s): %s",
          PROPERTY_WEBHOOK_URI,
          stringValue,
          e.getMessage()));
    }
  }

  private long setPauseBetweenRetries(Map<String, Object> properties) {
    final long _default = 60000;
    if (properties == null) {
      return _default;
    }

    final Object o = properties.get(PROPERTY_RETRY_PAUSE);
    if (o == null) {
      return _default;
    }

    try {
      final long longValue = Long.parseLong(String.valueOf(o));
      if (longValue > 0) {
        return longValue * 1000;
      } else {
        throw new IllegalArgumentException(String.format("Parameter %s must not be 0 or negative (%d).", PROPERTY_RETRY_PAUSE, longValue));
      }
    } catch (NumberFormatException e) {
      log.warn("Failed to process parameter {}: {}", PROPERTY_RETRY_PAUSE, e.getMessage());
      return _default;
    }
  }

  private int setRetries(Map<String, Object> properties) {
    final int _default = 1;
    if (properties == null) {
      return _default;
    }

    final Object o = properties.get(PROPERTY_RETRIES);
    if (o == null) {
      return _default;
    }

    try {
      final int intVal = Integer.parseInt(String.valueOf(o));
      if (intVal > 0) {
        return intVal;
      } else {
        throw new IllegalArgumentException(String.format("Parameter %s must be greater or equal 1 (%d).", PROPERTY_RETRIES, intVal));
      }
    } catch (NumberFormatException e) {
      log.warn("Failed to process parameter {}: {}", PROPERTY_RETRIES, e.getMessage());
      return _default;
    }
  }

}

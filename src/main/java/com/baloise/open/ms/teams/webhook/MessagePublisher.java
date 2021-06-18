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

import com.baloise.open.ms.teams.templates.MessageCard;

import java.util.HashMap;

public interface MessagePublisher {

  /**
   * defines the number of retires in case of technical interruptions
   */
  String PROPERTY_RETRIES = "com.baloise.open.ms.teams.retries";

  /**
   * defines the pause time between {@link #PROPERTY_RETRIES} in seconds
   */
  String PROPERTY_RETRY_PAUSE = "com.baloise.open.ms.teams.retries.pause";

  /**
   * defines the webhooks URI
   */
  String PROPERTY_WEBHOOK_URI = "com.baloise.open.ms.teams.webhook.uri";

  static MessagePublisher getInstance(final HashMap<String, Object> properties) {
    final HashMap<String, Object> defaultProperties = getDefaultProperties();
    if(properties != null) {
      properties.forEach((key, value) -> defaultProperties.merge(key, value, (k1, v1) -> value));
    }
    return new MessagePublisherImpl(properties);
  }

  void publish(MessageCard messageCard);

  static HashMap<String, Object> getDefaultProperties() {
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put(PROPERTY_RETRIES, 3);
    properties.put(PROPERTY_RETRY_PAUSE, 60);
    properties.put(PROPERTY_WEBHOOK_URI, null);
    return properties;
  }
}

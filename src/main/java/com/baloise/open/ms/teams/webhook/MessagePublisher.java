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
import com.baloise.open.ms.teams.templates.AdaptiveCard;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public interface MessagePublisher {

    /**
     * @deprecated use Config.PROPERTY_RETRIES instead
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    String PROPERTY_RETRIES = Config.PROPERTY_RETRIES;

    /**
     * @deprecated use Config.PROPERTY_RETRY_PAUSE instead
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    String PROPERTY_RETRY_PAUSE = Config.PROPERTY_RETRY_PAUSE;

    /**
     * @deprecated use Config.PROPERTY_WEBHOOK_URI instead
     */
    @Deprecated(forRemoval = true, since = "0.3.1")
    String PROPERTY_WEBHOOK_URI = Config.PROPERTY_WEBHOOK_URI;

    static MessagePublisher getInstance(String uri) {
        return getInstance(Config.builder().withWebhookURI(uri).build());
    }

    static MessagePublisher getInstance(String proxyUri, String uri) {
        return getInstance(Config.builder()
                .withWebhookURI(uri)
                .withProxyURI(proxyUri)
                .build());
    }

    static MessagePublisher getInstance(final Map<String, Object> customProperties) {
        final Config config = new Config(customProperties);
        return new MessagePublisherImpl(config);
    }

    static MessagePublisher getInstance(final Config config) {
        return new MessagePublisherImpl(config);
    }

    /**
     * transmits provided AdaptiveCard as parsed JSON String to webhook
     */
    ScheduledFuture<?> publish(AdaptiveCard adaptiveCard);

    /**
     * transmits provided jsonfied String to configured webhook
     */
    ScheduledFuture<?> publish(String jsonBody);
}

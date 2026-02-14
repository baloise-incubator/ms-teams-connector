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

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public interface MessagePublisher {

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
    ScheduledFuture<?> publish(SerializableMessage message);

    /**
     * transmits provided jsonfied String to configured webhook
     */
    ScheduledFuture<?> publish(String jsonBody);

    /**
     * Synchronously transmits the provided {@link SerializableMessage} to the configured webhook.
     * <p>
     * Unlike {@link #publish(SerializableMessage)}, this method blocks the calling thread until the
     * HTTP request has completed and any configured retry attempts (if supported by the implementation)
     * have either succeeded or been exhausted.
     * </p>
     * <p>
     * Implementations are expected to throw a runtime exception if the message cannot be delivered
     * successfully after all attempts (for example due to I/O errors, timeouts, or non-successful
     * HTTP status codes). No checked exceptions are declared on this interface; callers should consult
     * the concrete implementation for the exact exception types that may be thrown.
     * </p>
     *
     * @param message the message to publish synchronously, must not be {@code null}
     * @throws RuntimeException if delivery ultimately fails after all configured attempts
     */
    void publishSync(SerializableMessage message);

    /**
     * Synchronously transmits the provided JSON body to the configured webhook.
     * <p>
     * Unlike {@link #publish(String)}, this method blocks the calling thread until the
     * HTTP request has completed and any configured retry attempts (if supported by the implementation)
     * have either succeeded or been exhausted.
     * </p>
     * <p>
     * Implementations are expected to throw a runtime exception if the JSON payload cannot be delivered
     * successfully after all attempts (for example due to I/O errors, timeouts, or non-successful
     * HTTP status codes). No checked exceptions are declared on this interface; callers should consult
     * the concrete implementation for the exact exception types that may be thrown.
     * </p>
     *
     * @param jsonBody the JSON payload to publish synchronously, must not be {@code null}
     * @throws RuntimeException if delivery ultimately fails after all configured attempts
     */
    void publishSync(String jsonBody);

}

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
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@ToString
public final class Config {
    public static final long DEFAULT_PAUSE_BETWEEN_RETRIES = 60000;
    public static final int DEFAULT_RETRIES = 3;
    public static final boolean DEFAULT_BLOCKING = false;

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

    /**
     * defines the proxy URI
     */
    public static final String PROPERTY_PROXY_URI = "com.baloise.open.ms.teams.proxy.uri";

    /**
     * defines if sending is blocking or not.
     */
    public static final String PROPERTY_BLOCKING = "com.baloise.open.ms.teams.execution.blocking";

    private final int retries;
    private final long pauseBetweenRetries;
    private final URI webhookURI;
    private final URI proxyURI;
    private final boolean blocking;

    public Config(final Map<String, Object> properties) {
        retries = setRetries(properties);
        pauseBetweenRetries = setPauseBetweenRetries(properties);
        webhookURI = setWebhookURI(properties);
        proxyURI = setProxyURI(properties);
        blocking = setBlocking(properties);
    }

    private boolean setBlocking(Map<String, Object> properties) {
        if (properties != null) {
            Object propertyBlocking = properties.get(PROPERTY_BLOCKING);
            if (propertyBlocking != null) {
                String stringValue = String.valueOf(propertyBlocking);
                if (StringUtils.isNotBlank(stringValue)) {
                    return Boolean.parseBoolean(stringValue);
                }
            }
        }
        return DEFAULT_BLOCKING;
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

    private URI setProxyURI(Map<String, Object> properties) {
        final Object propertyObject = properties.get(PROPERTY_PROXY_URI);
        final String stringValue = String.valueOf(propertyObject);

        if (ObjectUtils.isNotEmpty(propertyObject) && StringUtils.isNotBlank(stringValue)) {
            try {
                return URI.create(stringValue);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format("Parameter %s must be a valid URI (%s): %s",
                        PROPERTY_PROXY_URI,
                        stringValue,
                        e.getMessage()));
            }
        }
        return null;
    }

    private long setPauseBetweenRetries(Map<String, Object> properties) {
        if (properties == null) {
            return DEFAULT_PAUSE_BETWEEN_RETRIES;
        }

        final Object o = properties.get(PROPERTY_RETRY_PAUSE);
        if (o == null) {
            return DEFAULT_PAUSE_BETWEEN_RETRIES;
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
            return DEFAULT_PAUSE_BETWEEN_RETRIES;
        }
    }

    private int setRetries(Map<String, Object> properties) {
        if (properties == null) {
            return DEFAULT_RETRIES;
        }

        final Object o = properties.get(PROPERTY_RETRIES);
        if (o == null) {
            return DEFAULT_RETRIES;
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
            return DEFAULT_RETRIES;
        }
    }

    public static Config.Builder builder() {
        return new Config.Builder();
    }

    public static class Builder {
        private final Map<String, Object> properties = new HashMap<>();

        public Builder withRetries(int retries) {
            properties.put(PROPERTY_RETRIES, retries);
            return this;
        }

        public Builder withPauseBetweenRetries(long pauseBetweenRetries) {
            properties.put(PROPERTY_RETRY_PAUSE, pauseBetweenRetries);
            return this;
        }

        public Builder withWebhookURI(URI webhookURI) {
            properties.put(PROPERTY_WEBHOOK_URI, webhookURI);
            return this;
        }

        public Builder withWebhookURI(String webhookURI) {
            properties.put(PROPERTY_WEBHOOK_URI, webhookURI);
            return this;
        }

        public Builder withProxyURI(URI proxyURI) {
            properties.put(PROPERTY_PROXY_URI, proxyURI);
            return this;
        }

        public Builder withProxyURI(String proxyURI) {
            properties.put(PROPERTY_PROXY_URI, proxyURI);
            return this;
        }

        public Builder withBlocking(boolean blocking) {
            properties.put(PROPERTY_BLOCKING, blocking);
            return this;
        }

        public Config build() {
            return new Config(properties);
        }
    }

}

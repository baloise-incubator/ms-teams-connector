package com.baloise.open.ms.teams.webhook;

/**
 * Custom exception for message publishing failures.
 */
public class MessagePublishingException extends RuntimeException {

    public MessagePublishingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessagePublishingException(String message) {
        super(message);
    }
}

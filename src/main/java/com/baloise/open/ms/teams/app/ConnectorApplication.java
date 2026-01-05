package com.baloise.open.ms.teams.app;

import com.baloise.open.ms.teams.Config;
import com.baloise.open.ms.teams.templates.AdaptiveCard;
import com.baloise.open.ms.teams.templates.AdaptiveCardFactory;
import com.baloise.open.ms.teams.templates.TeamsMessage;
import com.baloise.open.ms.teams.templates.TeamsMessageFactory;
import com.baloise.open.ms.teams.webhook.MessagePublisher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Entry point for publishing an Adaptive Card.
 * Reads exclusively environment variables (no CLI arguments anymore).
 *
 * Required fields: WEBHOOK_URL / MESSAGE_TITLE / MESSAGE_TEXT
 * Optional fields: MESSAGE_SUMMARY / MESSAGE_THEME_COLOR / RETRIES / RETRY_PAUSE_SEC
 *
 * Exit codes:
 *  0 Success / publish started
 *  2 Validation error (missing parameter)
 */
public class ConnectorApplication {

    private static final Logger log = LoggerFactory.getLogger(ConnectorApplication.class);
    private static final String DEFAULT_SUMMARY = "Automatically generated message";

    public static void main(String[] args) {
        int code = run();
        System.exit(code);
    }

    public static int run() {
        Map<String,String> env = System.getenv();
        String webhookUrl = trimToNull(env.get("WEBHOOK_URL"));
        String title = trimToNull(env.get("MESSAGE_TITLE"));
        String text = trimToNull(env.get("MESSAGE_TEXT"));
        String summary = trimToNull(env.get("MESSAGE_SUMMARY"));
        if (StringUtils.isBlank(summary)) {
            summary = DEFAULT_SUMMARY;
        }
        String themeColor = trimToNull(env.get("MESSAGE_THEME_COLOR"));
        Integer retriesVal = parseIntEnv(env.get("RETRIES"));
        Integer retryPauseSecVal = parseIntEnv(env.get("RETRY_PAUSE_SEC"));

        List<String> missing = new ArrayList<>();
        if (StringUtils.isBlank(webhookUrl)) missing.add("WEBHOOK_URL");
        if (StringUtils.isBlank(title)) missing.add("MESSAGE_TITLE");
        if (StringUtils.isBlank(text)) missing.add("MESSAGE_TEXT");
        if (!missing.isEmpty()) {
            log.error("Missing required environment variable(s): {}", String.join(", ", missing));
            return 2;
        }

        AdaptiveCardFactory factory = AdaptiveCardFactory.builder(title, text);
        if (StringUtils.isNotBlank(themeColor)) factory.withFact("ThemeColor", themeColor);
        AdaptiveCard card = factory.build();
        TeamsMessage message = TeamsMessageFactory.createTeamsMessageWithAdaptiveCard(summary, card);

        int retries = retriesVal != null ? retriesVal : Config.DEFAULT_RETRIES;
        long pauseMs = retryPauseSecVal != null ? retryPauseSecVal * 1000L : Config.DEFAULT_PAUSE_BETWEEN_RETRIES;

        log.info("Starting publish (webhook={}, retries={}, pauseSec={})", webhookUrl, retries, pauseMs/1000);
        Config config = Config.builder()
                .withWebhookURI(webhookUrl)
                .withRetries(retries)
                .withPauseBetweenRetries(pauseMs/1000)
                .build();
        MessagePublisher publisher = MessagePublisher.getInstance(config);
        publisher.publishSync(message);
        return 0;
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    static Integer parseIntEnv(String val) {
        if (StringUtils.isBlank(val)) return null;
        try { return Integer.parseInt(val.trim()); } catch (NumberFormatException e) {
            log.warn("Ignoring invalid integer env value '{}'", val);
            return null;
        }
    }
}

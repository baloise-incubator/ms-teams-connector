/*
 * Copyright 2025 Baloise Group
 */
package com.baloise.open.ms.teams.templates;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public final class TeamsMessageFactory {
    private TeamsMessageFactory() {}

    public static TeamsMessage createTeamsMessageWithAdaptiveCard(String summary, AdaptiveCard adaptiveCard) {
        if (StringUtils.isBlank(summary)) {
            throw new IllegalArgumentException("summary must not be blank");
        }
        if (adaptiveCard == null) {
            throw new IllegalArgumentException("adaptiveCard must not be empty");
        }
        Attachment attachment = Attachment.builder().content(adaptiveCard).build();
        return TeamsMessage.builder()
                .summary(summary)
                .attachments(List.of(attachment))
                .build();
    }
}


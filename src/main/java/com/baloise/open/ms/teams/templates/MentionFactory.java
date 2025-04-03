package com.baloise.open.ms.teams.templates;

import org.apache.commons.lang3.StringUtils;

public class MentionFactory {
    public static Mention createMention(String id, String name) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("id must not be null or empty");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name must not be null or empty");
        }

        String nameSnakeCase = name.trim().replace(StringUtils.SPACE, "_").toLowerCase();

        return Mention.builder()
                .text(String.format("<at>%s</at>", nameSnakeCase))
                .mentioned(MentionedPerson.builder()
                        .id(id)
                        .name(name)
                        .build())
                .build();
    }
}


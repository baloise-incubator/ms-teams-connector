/*
 * Copyright 2025 Baloise Group
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
package com.baloise.open.ms.teams.templates;

import org.apache.commons.lang3.StringUtils;

public class MentionFactory {

    private MentionFactory() {
        // static usage only
    }

    public static Mention createMention(String id, String name) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("id must not be null or empty");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name must not be null or empty");
        }

        final String nameSnakeCase = name.trim().replace(StringUtils.SPACE, "_").toLowerCase();

        return Mention.builder()
                .text("<at>%s</at>".formatted(nameSnakeCase))
                .mentioned(MentionedPerson.builder()
                        .id(id)
                        .name(name)
                        .build())
                .build();
    }
}


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

import com.baloise.open.ms.teams.json.JsonSerializableEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>A badge element to show an icon and/or text in a compact form over a colored background.</p> *
 * <a href="https://adaptivecards.microsoft.com/?topic=Badge" target="_blank">Badge reference</a>
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Badge extends AdaptiveObject {

    private final String type = Type.BADGE.getJsonValue();
    private String text;
    private String tooltip;
    private BadgeShape shape;
    private BadgeAppearance appearance;
    private BadgeStyle style;

    @Getter
    public enum BadgeShape implements JsonSerializableEnum {
        SQUARE("Square"),
        ROUNDED("Rounded"),
        CIRCULAR("Circular");

        private final String jsonValue;

        BadgeShape(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }

    @Getter
    public enum BadgeAppearance implements JsonSerializableEnum {
        FILLED("Filled"),
        TINT("Tint");

        private final String jsonValue;

        BadgeAppearance(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }

    @Getter
    public enum BadgeStyle implements JsonSerializableEnum {
        DEFAULT(JSON_VALUE_DEFAULT),
        SUBTLE("Subtle"),
        INFORMATIVE("Informative"),
        ACCENT("Accent"),
        GOOD("Good"),
        ATTENTION("Attention"),
        WARNING("Warning");

        private final String jsonValue;

        BadgeStyle(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }
}

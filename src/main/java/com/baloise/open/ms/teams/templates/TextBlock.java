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
 * <p>A block of text, optionally formatted using Markdown.</p>
 * <p>TextBlock supports a subset of the Commonmark Markdown syntax:</p>
 * <li>Bold: **Bold**</li>
 * <li>Italic: _Italic_</li>
 * <li>Bullet list: - Item 1\r- Item 2\r- Item 3</li>
 * <li>Numbered list: 1. Green\r2. Orange\r3. Blue</li>
 * <li>Hyperlink: [Hyperlink](https://www.microsoft.com)</li>
 * <p>All other Markdown formatting options are <b>not</b> supported.</p>
 *
 * <a href="https://adaptivecards.microsoft.com/?topic=TextBlock" target="_blank">TextBlock reference</a>
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TextBlock extends AdaptiveObject{

    private final String type = Type.TEXT_BLOCK.getJsonValue();
    private String text;
    private Boolean wrap = true;
    private TextWeight weight;
    private TextStyle style;
    private TextSize size;
    private TextFontType fontType;
    private TextColor color;

    @Getter
    public enum TextStyle implements JsonSerializableEnum {
        DEFAULT("Default"),
        COLUMN_HEADER("ColumnHeader"),
        HEADING("Heading");

        private final String jsonValue;

        TextStyle(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }

    @Getter
    public enum TextWeight implements JsonSerializableEnum {
        DEFAULT("Default"),
        LIGHTER("Lighter"),
        BOLDER("Bolder");

        private final String jsonValue;

        TextWeight(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }

    @Getter
    public enum TextSize implements JsonSerializableEnum {
        DEFAULT("Default"),
        SMALL("Small"),
        MEDIUM("Medium"),
        LARGE("Large"),
        EXTRA_LARGE("ExtraLarge");

        private final String jsonValue;

        TextSize(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }

    @Getter
    public enum TextFontType implements JsonSerializableEnum {
        DEFAULT("Default"),
        MONOSPACE("Monospace");
        private final String jsonValue;


        TextFontType(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }

    @Getter
    public enum TextColor implements JsonSerializableEnum {
        DEFAULT("Default"),
        DARK("Dark"),
        LIGHT("Light"),
        ACCENT("Accent"),
        GOOD("Good"),
        WARNING("Warning"),
        ATTENTION("Attention");

        private final String jsonValue;

        TextColor(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }
}

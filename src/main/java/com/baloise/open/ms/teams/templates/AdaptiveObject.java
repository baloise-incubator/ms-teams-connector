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
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@RequiredArgsConstructor
public abstract class AdaptiveObject {

    static final String JSON_VALUE_DEFAULT = "Default";

    private String id;
    private Boolean separator;
    private Spacing spacing;

    @Getter
    public enum Type implements JsonSerializableEnum {
        CODE_BLOCK("CodeBlock"),
        ADAPTIVE_CARD("AdaptiveCard"),
        BADGE("Badge"),
        CONTAINER("Container"),
        FACT_SET("FactSet"),
        CONTAINER_LAYOUT_FLOW("Layout.Flow"),
        MENTION("mention"),
        TEXT_BLOCK("TextBlock");

        private final String jsonValue;

        Type(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }
}

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

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <p>An Adaptive Card, containing a free-form body of card elements, and an optional set of actions.</p>
 * <a href="https://adaptivecards.microsoft.com/?topic=AdaptiveCard">AdaptiveCard reference</a>
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AdaptiveCard {
    private final String type = AdaptiveObject.Type.ADAPTIVE_CARD.getJsonValue();

    private final String version = "1.5";

    @SerializedName(value = "$schema")
    private final String schema = "http://adaptivecards.io/schemas/adaptive-card.json";

    @SerializedName(value = "msteams")
    @Builder.Default
    private TeamsCardProperties teamsCardProperties = new TeamsCardProperties();

    private List<AdaptiveObject> body;
}

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <p>Represents a set of Teams-specific properties on a card.</p>
 * <a href="https://adaptivecards.microsoft.com/?topic=TeamsCardProperties" target="_blank">TeamsCardProperties reference</a>
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TeamsCardProperties {
    private List<Mention> entities;
    @Builder.Default
    private TeamCardWidth width = TeamCardWidth.Full;

    public enum TeamCardWidth {
        Full
    }
}

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
package com.baloise.open.ms.teams.templates.card.adaptive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * <p>Represents a mentioned person.</p>
 * <a href="https://adaptivecards.microsoft.com/?topic=MentionedPersion" target="_blank">MentionedPersion reference</a>
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MentionedPerson {
    /**
     * <p>The Id of a mentioned person entity, typically a Microsoft Entra user Id.</p>
     *
     * <p>For a person this is normally their email address.</p>
     */
    private String id;
    /**
     * The name of the mentioned person.
     */
    private String name;
}

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

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>A set of facts, displayed as a table or a vertical list when horizontal space is constrained.</p> *
 * <p>
 *     Use FactSet to display a series of title and value pairs in a simple table layout.
 *     FactSet is responsive out of the box and will adjust its layout to a vertical list as the card becomes narrower.
 * </p>
 * <a href="https://adaptivecards.microsoft.com/?topic=FactSet" target="_blank">FactSet reference</a>
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FactSet extends AdaptiveObject{
    private final String type = Type.FACT_SET.getJsonValue();
    private List<Fact> facts;
}

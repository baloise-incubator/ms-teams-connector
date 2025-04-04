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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * <p>A container for other elements. Use containers for styling purposes and/or to logically group a set of elements together.</p> *
 * <a href="https://adaptivecards.microsoft.com/?topic=Container" target="_blank">Container reference</a>
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Container extends AdaptiveObject{

    private final String type = Type.CONTAINER.getJsonValue();
    private List<AdaptiveObject> items;
    private List<ContainerLayout> layouts;
    private ContainerStyle style;
    private Boolean roundedCorners;
    private Boolean showBorder;

    public enum ContainerStyle {
        Default,
        Emphasis,
        Accent,
        Good,
        Attention,
        Warning
    }
}

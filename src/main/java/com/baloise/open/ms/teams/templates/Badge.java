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

    public enum BadgeShape {
        Square,
        Rounded,
        Circular,
    }

    public enum BadgeAppearance {
        Filled,
        Tint
    }

    public enum BadgeStyle {
        Default,
        Subtle,
        Informative,
        Accent,
        Good,
        Attention,
        Warning
    }
}

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

import com.baloise.open.ms.teams.json.JsonSerializableEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>A layout that spreads elements horizontally and wraps them across multiple rows, as needed.</p>
 * <p>
 *     Use Layout.Flow to layout lists of elements such as images without having to worry about the amount of horizontal space available.
 *     The width and alignment of elements can be tuned as desired. Each row automatically gets the appropriate height, and spacing between columns and rows is configurable and automatically enforced.
 * </p>
 *
 * <a href="https://adaptivecards.microsoft.com/?topic=Layout.Flow" target="_blank">Layout.Flow reference</a>
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FlowContainerLayout extends ContainerLayout {

    private final String type = Type.CONTAINER_LAYOUT_FLOW.getJsonValue();
    private HorizontalItemsAlignment horizontalItemsAlignment;
    private VerticalItemsAlignment verticalItemsAlignment;
    private Spacing rowSpacing;
    private Spacing columnSpacing;
    private ItemFit itemFit;

    /**
     * <p>The width, in pixels, of each item, in the <number>px format. Should not be used if maxItemWidth and/or minItemWidth are set.</p>
     *
     * Valid values: "<number>px"
     */
    private String itemWidth;

    /**
     * <p>The maximum width, in pixels, of each item, in the <number>px format. Should not be used if itemWidth is set.</p>
     *
     * Valid values: "<number>px"
     */
    private String minItemWidth;

    /**
     * <p>The maximum width, in pixels, of each item, in the <number>px format. Should not be used if itemWidth is set.</p>
     *
     * Valid values: "<number>px"
     */
    private String maxItemWidth;

    @Getter
    public enum ItemFit implements JsonSerializableEnum {
        FIT("Fit"),
        FILL("Fill");

        private final String jsonValue;

        ItemFit(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }

    @Getter
    public enum HorizontalItemsAlignment implements JsonSerializableEnum {
        LEFT("Left"),
        CENTER("Center"),
        RIGHT("Right");

        private final String jsonValue;

        HorizontalItemsAlignment(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }

    @Getter
    public enum VerticalItemsAlignment implements JsonSerializableEnum {
        TOP("Top"),
        CENTER("Center"),
        BOTTOM("Bottom");

        private final String jsonValue;

        VerticalItemsAlignment(final String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }
}

package com.baloise.open.ms.teams.templates;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    public static final String TYPE = "Layout.Flow";

    private final String type = TYPE;
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

    public enum ItemFit {
        Fit,
        Fill
    }

    public enum HorizontalItemsAlignment {
        Left,
        Center,
        Right
    }

    public enum VerticalItemsAlignment {
        Top,
        Center,
        Bottom
    }
}

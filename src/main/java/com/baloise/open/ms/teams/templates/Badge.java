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
    public static final String TYPE = "Badge";

    private final String type = TYPE;
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

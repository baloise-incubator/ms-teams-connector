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
    public static final String TYPE = "Container";

    private final String type = TYPE;
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

package com.baloise.open.ms.teams.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
    public static final String TYPE = "FactSet";

    private final String type = TYPE;
    private List<Fact> facts;
}

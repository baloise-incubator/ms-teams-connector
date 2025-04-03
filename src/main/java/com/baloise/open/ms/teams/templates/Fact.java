package com.baloise.open.ms.teams.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * <p>A fact in a FactSet element.</p> *
 * <a href="https://adaptivecards.microsoft.com/?topic=Fact" target="_blank">Fact reference</a>
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Fact {
    private String title;
    private String value;
}


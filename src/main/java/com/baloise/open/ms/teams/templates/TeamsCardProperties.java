package com.baloise.open.ms.teams.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <p>Represents a set of Teams-specific properties on a card.</p>
 * <a href="https://adaptivecards.microsoft.com/?topic=TeamsCardProperties" target="_blank">TeamsCardProperties reference</a>
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TeamsCardProperties {
    private List<Mention> entities;
    @Builder.Default
    private TeamCardWidth width = TeamCardWidth.Full;

    public enum TeamCardWidth {
        Full
    }
}

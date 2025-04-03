package com.baloise.open.ms.teams.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * <p>Represents a mentioned person.</p>
 * <a href="https://adaptivecards.microsoft.com/?topic=MentionedPersion" target="_blank">MentionedPersion reference</a>
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MentionedPerson {
    /**
     * <p>The Id of a mentioned person entity, typically a Microsoft Entra user Id.</p>
     *
     * <p>For a person this is normally their email address.</p>
     */
    private String id;
    /**
     * The name of the mentioned person.
     */
    private String name;
}

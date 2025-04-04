package com.baloise.open.ms.teams.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * <p>Represents a mention to a person.</p>
 * <p>To include an @mention, your card needs to include the following:</p>
 *
 * <ul>
 *   <li>A TextBlock element whose text property contains one or more @mention(s) in the {@code <at>any text</at>} format</li>
 *   <li>
 *       Matching mention objects in the entities collection of the card's msTeams property. Each object must include:
 *       <ul>
 *         <li>The text property that matches the @mention as included in the TextBlock's text (e.g. "<{@code <at>any text</at>}")</li>
 *         <li>
 *             The mentioned.id property, uniquely identifying the user being mentioned
 *             (see <a href="https://learn.microsoft.com/en-us/microsoftteams/platform/bots/how-to/conversations/send-proactive-messages?tabs=dotnet#get-the-microsoft-entra-user-id-user-id-team-id-or-channel-id">Get the Microsoft Entra user ID, user ID, team ID, or channel ID</a>)
 *         </li>
 *       </ul>
 *   </li>
 * </ul>
 *
 * <p>Teams platform allows you to use the following types of Ids in @mentions:</p>
 * <ul>
 *   <li>Microsoft Entra Object ID: 49c4641c-ab91-4248-aebb-6a7de286397b</li>
 *   <li>Microsoft Entra UPN: john.smith@microsoft.com</li>
 * </ul>
 *
 * <a href="https://adaptivecards.microsoft.com/?topic=mention" target="_blank">mention reference</a>
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Mention {
    public static final String TYPE = "mention";

    private final String type = TYPE;
    private MentionedPerson mentioned;
    /**
     * The text that will be substituted with the mention.
     */
    private String text;
}

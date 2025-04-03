package com.baloise.open.ms.teams.templates;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <p>An Adaptive Card, containing a free-form body of card elements, and an optional set of actions.</p>
 * <a href="https://adaptivecards.microsoft.com/?topic=AdaptiveCard">AdaptiveCard reference</a>
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AdaptiveCard {
    public static final String TYPE = "AdaptiveCard";

    private final String type = TYPE;

    private final String version = "1.5";

    @SerializedName(value = "$schema")
    private final String schema = "http://adaptivecards.io/schemas/adaptive-card.json";

    @SerializedName(value = "msteams")
    @Builder.Default
    private TeamsCardProperties teamsCardProperties = new TeamsCardProperties();

    private List<AdaptiveObject> body;
}

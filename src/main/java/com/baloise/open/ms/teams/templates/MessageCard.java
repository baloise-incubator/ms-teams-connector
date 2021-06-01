package com.baloise.open.ms.teams.templates;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class MessageCard {

  @SerializedName(value = "@type")
  private final String type = "MessageCard";

  @SerializedName(value = "@context")
  private final String context = "http://schema.org/extensions";

  private String themeColor = "0076D7";
  private final String title;
  private final String summary;

  private List<Section> sections;

  @SerializedName(value = "potentialAction")
  private List<ActionCard> potentialActions;

  public void addSection(Section section) {
    if (sections == null) {
      sections = new ArrayList<>();
    }
    this.sections.add(section);
  }

  public void addPotentialAction(ActionCard actionCard) {
    if (potentialActions == null) {
      potentialActions = new ArrayList<>();
    }
    potentialActions.add(actionCard);
  }
}

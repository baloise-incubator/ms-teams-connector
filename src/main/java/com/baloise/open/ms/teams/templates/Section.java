package com.baloise.open.ms.teams.templates;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Section {

  private String activityTitle;
  private String activitySubtitle;
  private String activityImage;
  private final List<Fact> facts = new ArrayList<>();
  private Boolean markdown;

  public void addFact(Fact fact) {
    facts.add(fact);
  }
}

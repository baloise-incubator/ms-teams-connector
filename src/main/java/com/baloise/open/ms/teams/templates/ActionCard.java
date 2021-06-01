package com.baloise.open.ms.teams.templates;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActionCard {

  @SerializedName(value = "@type")
  private final String type = "ActionCard";

  private final String name;

  private List<Input> inputs;
  private List<Action> actions;

  public void addInput(Input input) {
    if (inputs == null) {
      inputs = new ArrayList<>();
    }
    inputs.add(input);
  }

  public void addAction(Action action) {
    if (actions == null) {
      actions = new ArrayList<>();
    }
    actions.add(action);
  }
}

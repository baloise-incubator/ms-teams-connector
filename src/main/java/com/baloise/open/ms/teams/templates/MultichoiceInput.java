package com.baloise.open.ms.teams.templates;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MultichoiceInput extends Input {

  public MultichoiceInput(String id, String title) {
    super(id, title);
  }

  public MultichoiceInput(String id, String title, boolean isMultiSelect) {
    super(id, title);
    this.isMultiSelect = isMultiSelect;
  }

  @SerializedName(value = "@type")
  private String type = "MultichoiceInput";

  private boolean isMultiSelect;
  private List<Choice> choices;

  public void addChoice(Choice choice) {
    if (choices == null) {
      choices = new ArrayList<>();
    }
    choices.add(choice);
  }
}

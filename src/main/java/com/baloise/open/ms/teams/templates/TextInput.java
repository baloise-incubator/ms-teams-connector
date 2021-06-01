package com.baloise.open.ms.teams.templates;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextInput extends Input{

  public TextInput(String id, String title) {
    super(id, title);
  }

  public TextInput(String id, String title, boolean isMultiline) {
    super(id, title);
    this.isMultiline = isMultiline;
  }

  @SerializedName(value = "@type")
  private final String type = "TextInput";

  private boolean isMultiline;
}

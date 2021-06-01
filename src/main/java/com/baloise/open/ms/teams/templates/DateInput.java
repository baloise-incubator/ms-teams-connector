package com.baloise.open.ms.teams.templates;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
public class DateInput extends Input{

  public DateInput(String id, String title) {
    super(id, title);
  }

  @SerializedName(value = "@type")
  private final String type = "DateInput";

}

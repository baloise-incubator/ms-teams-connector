package com.baloise.open.ms.teams.templates;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Action {

  @SerializedName(value = "@type")
  private final Type type = Type.HttpPOST;

  private final String name;
  private final String target;

  public enum Type {
    HttpPOST
  }
}

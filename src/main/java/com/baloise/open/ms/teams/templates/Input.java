package com.baloise.open.ms.teams.templates;

import lombok.Getter;

@Getter
public abstract class Input {

  public Input(String id, String title) {
    this.id = id;
    this.title = title;
  }

  final String id;
  final String title;
}

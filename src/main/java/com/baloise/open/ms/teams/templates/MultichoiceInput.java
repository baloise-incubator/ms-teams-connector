/*
 * Copyright 2018 - 2021 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

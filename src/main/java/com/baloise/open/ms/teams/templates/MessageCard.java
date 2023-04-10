/*
 * Copyright 2021 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

  private String themeColor;
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

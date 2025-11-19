/*
 * Copyright 2025 Baloise Group
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

import com.baloise.open.ms.teams.templates.card.message.Fact;
import com.baloise.open.ms.teams.templates.card.message.MessageCard;
import com.baloise.open.ms.teams.templates.card.message.PotentialAction;
import com.baloise.open.ms.teams.templates.card.message.Section;
import java.util.ArrayList;
import java.util.List;

public class MessageCardFactory {
  private final String title;
  private final String message;
  private List<Fact> facts;
  private List<PotentialAction> potentialActions;
  private boolean markdown = false;
  private String image = null;
  private String themeColor = null;

  private MessageCardFactory(String title, String message) {
    this.title = title;
    this.message = message;
  }

  public static MessageCardFactory builder(String title, String message) {
    return new MessageCardFactory(title, message);
  }

  public MessageCard build() {
    var section = new Section(title, message, image, facts, markdown);
    return new MessageCard(themeColor, title, List.of(section), potentialActions);
  }

  public MessageCardFactory withMarkdown() {
    this.markdown = true;
    return this;
  }

  public MessageCardFactory withImage(String image) {
    this.image = image;
    return this;
  }

  public MessageCardFactory withThemeColor(String themeColor) {
    this.themeColor = themeColor;
    return this;
  }

  public MessageCardFactory withFact(Fact fact) {
    if (this.facts == null) {
      this.facts = new ArrayList<>();
    }

    this.facts.add(fact);
    return this;
  }

  public MessageCardFactory withPotentialAction(PotentialAction potentialAction) {
    if (this.potentialActions == null) {
      this.potentialActions = new ArrayList<>();
    }

    this.potentialActions.add(potentialAction);
    return this;
  }


}

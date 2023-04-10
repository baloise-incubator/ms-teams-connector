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

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public final class MessageCardFactory {

  enum Color {
    RED("#ff0000"),
    GREEN("#1aff00"),
    YELLOW("#ffee00"),
    BLUE("#0600ab");

    final String hexCode;

    Color(String hexCode) {
      this.hexCode = hexCode;
    }
  }

  final MessageCard instance;

  private MessageCardFactory(String title, String message) {
    instance = createSimpleMessageCard(title, message);
  }

  public static MessageCard createSimpleMessageCard(final String title, final String message) {
    final MessageCard messageCard = new MessageCard(null, title);
    messageCard.addSection(Section.builder().activityTitle(title).activitySubtitle(message).build());
    return messageCard;
  }

  public static MessageCard createSimpleMessageCard(final String title, final String message, Map<String, Object> facts) {
    final MessageCard messageCard = createSimpleMessageCard(title, message);
    messageCard.getSections().get(0).addFacts(facts);
    return messageCard;
  }

  public static MessageCardFactory builder(final String title, final String message) {
    return new MessageCardFactory(title, message);
  }

  public MessageCardFactory withColor(final Color color) {
    if (color != null) {
      instance.setThemeColor(color.hexCode);
    }
    return this;
  }

  public MessageCardFactory withFact(final Fact fact) {
    if (fact != null) {
      instance.getSections().get(0).getFacts().add(fact);
    }
    return this;
  }

  public MessageCardFactory withFact(final String key, final String value) {
    if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
      return withFact(new Fact(key, value));
    }
    return this;
  }

  public MessageCard build() {
    return instance;
  }
}

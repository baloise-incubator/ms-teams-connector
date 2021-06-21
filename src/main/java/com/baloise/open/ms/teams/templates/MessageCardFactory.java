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

public final class MessageCardFactory {

  private MessageCardFactory() {
    // Do not instantiate
  }

  public static MessageCard createSimpleMessageCard(final String title, final String message) {
    final MessageCard messageCard = new MessageCard(null, title);
    messageCard.addSection(Section.builder().activityTitle(title).activitySubtitle(message).build());
    return messageCard;
  }
}

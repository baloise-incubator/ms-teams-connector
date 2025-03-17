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

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class Section {

  private String activityTitle;
  private String activitySubtitle;
  private String activityImage;
  @Getter(AccessLevel.NONE)
  private final List<Fact> facts = new ArrayList<>();
  private Boolean markdown;

  public void addFact(Fact fact) {
    if (!facts.contains(fact)) {
      facts.add(fact);
    }
  }

  public void addFacts(final Map<String, Object> factProperties) {
    if (factProperties == null || factProperties.isEmpty()) {
      return;
    }

    factProperties.forEach((name, value) -> addFact(new Fact(name, String.valueOf(value))));
  }

  public List<Fact> getFacts() {
    return List.copyOf(facts);
  }
}

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
package com.baloise.open.ms.teams.templates.card.message;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public record MultichoiceInput(
  String id,
  String title,
  String isMultiSelect,
  List<Choice> choices
) implements Input {
  @SerializedName("@type")
  public String type() {
    return "MultichoiceInput";
  }
}

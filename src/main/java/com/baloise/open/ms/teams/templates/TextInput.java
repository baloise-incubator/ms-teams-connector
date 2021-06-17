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

@Getter
@Setter
public class TextInput extends Input{

  public TextInput(String id, String title) {
    super(id, title);
  }

  public TextInput(String id, String title, boolean isMultiline) {
    super(id, title);
    this.isMultiline = isMultiline;
  }

  @SerializedName(value = "@type")
  private final String type = "TextInput";

  private boolean isMultiline;
}

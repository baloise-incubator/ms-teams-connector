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

import com.baloise.open.ms.teams.json.Serializer;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class TeamsMessageTest {

    @Test
    void shouldSerializeMessageWithAdaptiveCardAttachment() throws Exception {
        TeamsMessage message = TeamsMessageFactory.createTeamsMessageWithAdaptiveCard("MySummary", AdaptiveCardFactory.createSimpleAdaptiveCard("MyTitle","MyText"));

        JsonElement tree = Serializer.asJsonTree(message);
        String actualJson = tree.toString();

        String expectedJson;
        try (InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("teams_message_adaptive_card_template.json"), "Resource not found")) {
            expectedJson = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

  @Test
  void shouldFailOnBlankSummary() {
    Executable exec = () -> TeamsMessageFactory.createTeamsMessageWithAdaptiveCard(" ", AdaptiveCardFactory.createSimpleAdaptiveCard("MyTitle","Hello"));
    assertThrows(IllegalArgumentException.class, exec);
  }

  @Test
  void shouldFailOnNullAdaptiveCard() {
    Executable exec = () -> TeamsMessageFactory.createTeamsMessageWithAdaptiveCard("MySummary", null);
    assertThrows(IllegalArgumentException.class, exec);
  }
}

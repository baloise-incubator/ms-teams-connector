/*
 * Copyright 2025 Baloise Group
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
}

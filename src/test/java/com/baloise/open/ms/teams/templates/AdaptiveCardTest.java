package com.baloise.open.ms.teams.templates;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AdaptiveCardTest {
    @Test
    void verifyBasicTemplate() {
        // read expected json file from resource
        try (final InputStream resourceAsStream = ClassLoader.getSystemResourceAsStream("adaptive_card_template.json");
             final InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder expected = new StringBuilder();
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                expected.append(line).append(System.lineSeparator());
            }

            AdaptiveCard adaptiveCard = AdaptiveCard.builder()
                    .body(List.of(
                            TextBlock.builder()
                                    .text("Hello World")
                                    .weight(TextBlock.TextWeight.Bolder)
                                    .style(TextBlock.TextStyle.Heading)
                                    .build(),
                            FactSet.builder()
                                    .facts(List.of(
                                            Fact.builder()
                                                    .title("Fact 1")
                                                    .value("Value 1")
                                                    .build(),
                                            Fact.builder()
                                                    .title("Fact 2")
                                                    .value("Value 2")
                                                    .build()
                                    ))
                                    .build()
                    ))
                    .build();

            //compare the result
            assertEquals(JsonParser.parseString(expected.toString()), new Gson().toJsonTree(adaptiveCard));
        } catch (Exception e) {
            fail("failed to read message_card_template for comparison: " + e.getMessage());
        }
    }
}
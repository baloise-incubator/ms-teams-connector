package com.baloise.open.ms.teams.templates.card.adaptive;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.baloise.open.ms.teams.json.Serializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactTest extends PropertyReflectionTest {

    private final Fact testee = Fact.builder()
            .title("title")
            .value("value")
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(Fact.class, 2);
    }

    @Test
    void verifyDefaults() {
        assertEquals("title", testee.getTitle());
        assertEquals("value", testee.getValue());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"title\":\"title\",\"value\":\"value\"}",
                Serializer.asJson(testee)
        );
    }
}

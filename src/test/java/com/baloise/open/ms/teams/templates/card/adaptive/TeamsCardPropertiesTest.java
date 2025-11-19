package com.baloise.open.ms.teams.templates.card.adaptive;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.baloise.open.ms.teams.json.Serializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TeamsCardPropertiesTest extends PropertyReflectionTest {

    private final TeamsCardProperties testee = TeamsCardProperties.builder().build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(TeamsCardProperties.class, 2);
    }

    @Test
    void verifyDefaults() {
        assertEquals("full", testee.getWidth());
        assertNull(testee.getEntities());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"width\":\"full\"}",
                Serializer.asJson(testee)
        );
    }
}

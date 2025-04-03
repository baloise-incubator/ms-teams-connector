package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
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
        assertEquals(TeamsCardProperties.TeamCardWidth.Full, testee.getWidth());
        assertNull(testee.getEntities());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"width\":\"Full\"}",
                new GsonBuilder().create().toJson(testee)
        );
    }
}

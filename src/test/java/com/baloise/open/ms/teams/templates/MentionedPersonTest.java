package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MentionedPersonTest extends PropertyReflectionTest {

    private final MentionedPerson testee = MentionedPerson.builder()
            .id("id")
            .name("name")
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(MentionedPerson.class, 2);
    }

    @Test
    void verifyDefaults() {
        assertEquals("id", testee.getId());
        assertEquals("name", testee.getName());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"id\":\"id\",\"name\":\"name\"}",
                new GsonBuilder().create().toJson(testee)
        );
    }
}

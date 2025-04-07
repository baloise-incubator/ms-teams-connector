package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.baloise.open.ms.teams.json.Serializer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactSetTest extends PropertyReflectionTest {

    private final FactSet testee = FactSet.builder()
            .facts(List.of(Fact.builder()
                    .title("title")
                    .value("value")
                    .build()))
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(FactSet.class, 2);
    }

    @Test
    void verifyDefaults() {
        assertEquals("FactSet", testee.getType());
        assertEquals(List.of(Fact.builder()
                .title("title")
                .value("value")
                .build()), testee.getFacts());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"type\":\"FactSet\",\"facts\":[{\"title\":\"title\",\"value\":\"value\"}]}",
                Serializer.asJson(testee)
        );
    }
}

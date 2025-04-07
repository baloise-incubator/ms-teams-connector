package com.baloise.open.ms.teams.templates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.baloise.open.ms.teams.json.Serializer;
import java.util.List;
import org.junit.jupiter.api.Test;

class FactSetTest extends PropertyReflectionTest {

  private final FactSet testee = FactSet.builder()
                                   .facts(List.of(new Fact("title", "value")))
                                   .build();

  @Test
  void verifyNumberOfProperties() {
    this.assertNumberOfProperties(FactSet.class, 2);
  }

  @Test
  void verifyDefaults() {
    assertEquals("FactSet", testee.getType());
    assertEquals(List.of(new Fact("title", "value")), testee.getFacts());
  }

  @Test
  void verifySerializaion() {
    assertEquals(
      "{\"type\":\"FactSet\",\"facts\":[{\"title\":\"title\",\"value\":\"value\"}]}",
      Serializer.asJson(testee)
    );
  }
}

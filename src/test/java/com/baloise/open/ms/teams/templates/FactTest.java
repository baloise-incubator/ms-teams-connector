package com.baloise.open.ms.teams.templates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.baloise.open.ms.teams.json.Serializer;
import org.junit.jupiter.api.Test;

class FactTest extends PropertyReflectionTest {

  private final Fact testee = new Fact("title", "value");

  @Test
  void verifyNumberOfProperties() {
    this.assertNumberOfProperties(Fact.class, 2);
  }

  @Test
  void verifyDefaults() {
    assertEquals("title", testee.title());
    assertEquals("value", testee.value());
  }

  @Test
  void verifySerializaion() {
    assertEquals(
      "{\"title\":\"title\",\"value\":\"value\"}",
      Serializer.asJson(testee)
    );
  }
}

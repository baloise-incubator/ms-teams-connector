package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateInputTest extends PropertyReflectionTest {

  private final DateInput testee = new DateInput("id", "title");

  @Test
  void verifyNumberOfProperties() {
    this.assertNumberOfProperties(Input.class, 2);
    this.assertNumberOfProperties(DateInput.class, 1);
  }

  @Test
  void verifyDefaults() {
    assertEquals("DateInput", testee.getType());
    assertEquals("id", testee.getId());
    assertEquals("title", testee.getTitle());
  }

  @Test
  void verifySerializaion() {
    assertEquals("{\"@type\":\"DateInput\",\"id\":\"id\",\"title\":\"title\"}",
        new GsonBuilder().create().toJson(testee));
  }

}

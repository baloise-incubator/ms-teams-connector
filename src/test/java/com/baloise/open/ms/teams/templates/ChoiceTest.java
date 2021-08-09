package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChoiceTest extends PropertyReflectionTest {

  private final Choice testee = new Choice("myDisplayName", "myValue");

  @Test
  void verifyNumberOfProperties() {
    this.assertNumberOfProperties(Choice.class ,2);
  }

  @Test
  void verifyDefaults() {
    assertEquals("myDisplayName", testee.getDisplay());
    assertEquals("myValue", testee.getValue());
  }

  @Test
  void verifySerializaion() {
    assertEquals("{\"display\":\"myDisplayName\",\"value\":\"myValue\"}",
        new GsonBuilder().create().toJson(testee));
  }
}

package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionTest extends PropertyReflectionTest {

  private final Action testee = new Action("myName", "myTarget");

  @Test
  void verifyNumberOfProperties() {
    this.assertNumberOfProperties(Action.class ,3);
  }

  @Test
  void verifyDefaults() {
    assertEquals("HttpPOST", testee.getType());
    assertEquals("myName", testee.getName());
    assertEquals("myTarget", testee.getTarget());
  }

  @Test
  void verifySerializaion() {
     assertEquals("{\"@type\":\"HttpPOST\",\"name\":\"myName\",\"target\":\"myTarget\"}",
         new GsonBuilder().create().toJson(testee));
  }
}

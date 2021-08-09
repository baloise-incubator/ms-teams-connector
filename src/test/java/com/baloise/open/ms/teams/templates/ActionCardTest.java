package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionCardTest extends PropertyReflectionTest {

  private static final TextInput TEST_INPUT = new TextInput("id", "title");
  private static final Action TEST_ACTION = new Action("ActionName", "actionTarget");


  @Test
  void verifyNumberOfProperties() {
    this.assertNumberOfProperties(ActionCard.class ,4);
  }

  @Test
  void verifyDefaults() {
    final ActionCard testee = new ActionCard("myName");
    assertEquals("ActionCard", testee.getType());
    assertEquals("myName", testee.getName());
    assertNull(testee.getInputs());
    assertNull(testee.getActions());
  }

  @Test
  void verifyInputs() {
    final ActionCard testee = new ActionCard("MyInputTest");
    assertNull(testee.getInputs());
    testee.addInput(TEST_INPUT);
    assertNotNull(testee.getInputs());
    assertEquals(1, testee.getInputs().size());
    assertEquals(TEST_INPUT, testee.getInputs().get(0));
  }
  
  @Test
  void verifyAcions() {
    final ActionCard testee = new ActionCard("MyActionsTest");
    assertNull(testee.getActions());
    testee.addAction(TEST_ACTION);
    assertNotNull(testee.getActions());
    assertEquals(1, testee.getActions().size());
    assertEquals(TEST_ACTION, testee.getActions().get(0));
  }

  @Test
  void verifySerializaion() {
    final ActionCard testee = new ActionCard("myName");
    assertEquals("{\"@type\":\"ActionCard\",\"name\":\"myName\"}",
        new GsonBuilder().create().toJson(testee));
    testee.addInput(TEST_INPUT);
    testee.addAction(TEST_ACTION);
    assertEquals("{\"@type\":\"ActionCard\",\"name\":\"myName\",\"inputs\":[{\"@type\":\"TextInput\",\"isMultiline\":false,\"id\":\"id\",\"title\":\"title\"}],\"actions\":[{\"@type\":\"HttpPOST\",\"name\":\"ActionName\",\"target\":\"actionTarget\"}]}",
        new GsonBuilder().create().toJson(testee));
  }
  
}

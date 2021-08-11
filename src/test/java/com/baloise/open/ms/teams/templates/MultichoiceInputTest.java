package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultichoiceInputTest extends PropertyReflectionTest {

  private static final Choice TEST_CHOICE = new Choice("x", "y");
  private final MultichoiceInput testee = new MultichoiceInput("myId", "myTitle");

  @Test
  void verifyNumberOfProperties() {
    assertAll(
        () -> this.assertNumberOfProperties(Input.class, 2),
        () -> this.assertNumberOfProperties(MultichoiceInput.class, 3)
    );
  }

  @Test
  void verifyDefaults() {
    assertEquals("MultichoiceInput", testee.getType());
    assertEquals("myId", testee.getId());
    assertEquals("myTitle", testee.getTitle());
    assertFalse(testee.isMultiSelect());
    assertTrue(new MultichoiceInput("myId", "myTitle" ,true).isMultiSelect());
    assertNull(testee.getChoices());
  }

  @Test
  void verifyChoices() {
    final MultichoiceInput testee = new MultichoiceInput("myId", "myTitle");
    assertNull(testee.getChoices());
    testee.addChoice(TEST_CHOICE);
    assertNotNull(testee.getChoices());
    assertEquals(1, testee.getChoices().size());
    assertEquals(TEST_CHOICE, testee.getChoices().get(0));
  }

  @Test
  void verifySerializaion() {
    assertEquals("{\"@type\":\"MultichoiceInput\",\"isMultiSelect\":false,\"id\":\"myId\",\"title\":\"myTitle\"}",
        new GsonBuilder().create().toJson(testee));
    final MultichoiceInput multichoiceInput = new MultichoiceInput("myId", "myTitle");
    multichoiceInput.addChoice(TEST_CHOICE);
    assertEquals("{\"@type\":\"MultichoiceInput\",\"isMultiSelect\":false,\"choices\":[{\"display\":\"x\",\"value\":\"y\"}],\"id\":\"myId\",\"title\":\"myTitle\"}",
        new GsonBuilder().create().toJson(multichoiceInput));

  }
}

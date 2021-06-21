package com.baloise.open.ms.teams.templates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageCardFactoryTest {

  private static final String SCHEMA = "http://schema.org/extensions";
  private static final String COLOR = "0076D7";
  private static final String TYPE = "MessageCard";


  @Test
  void testSimpleMessageCreation() {
    final String title = "My Title to set";
    final String text = "And the content to be verified";

    final MessageCard simpleMessageCard = MessageCardFactory.createSimpleMessageCard(title, text);

    final Section section = simpleMessageCard.getSections().get(0);

    assertAll(
        () -> assertEquals(title, simpleMessageCard.getTitle()),
        () -> assertEquals(title, simpleMessageCard.getSummary()),
        () -> assertEquals(SCHEMA, simpleMessageCard.getContext()),
        () -> assertEquals(COLOR, simpleMessageCard.getThemeColor()),
        () -> assertEquals(TYPE, simpleMessageCard.getType()),
        () -> assertNull(simpleMessageCard.getPotentialActions()),
        () -> assertEquals(1, simpleMessageCard.getSections().size()),
        () -> assertEquals(title, section.getActivityTitle()),
        () -> assertEquals(text, section.getActivitySubtitle()),
        () -> assertNull(section.getActivityImage()),
        () -> assertNull(section.getMarkdown()),
        () -> assertEquals(0, section.getFacts().size())
    );
  }
}
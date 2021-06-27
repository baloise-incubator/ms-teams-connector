package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.webhook.MessagePublisher;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        () -> assertNull(simpleMessageCard.getTitle()),
        () -> assertEquals(title, simpleMessageCard.getSummary()),
        () -> assertEquals(SCHEMA, simpleMessageCard.getContext()),
        () -> assertNull(simpleMessageCard.getThemeColor()),
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

  @Test
  void testSimpleMessageCreationWithFacts() {
    final String title = "My Title to set";
    final String text = "And the content to be verified";

    final MessageCard messageCard = MessageCardFactory.createSimpleMessageCard(title, text, MessagePublisher.getDefaultProperties());
    messageCard.getSections().get(0).addFacts(MessagePublisher.getDefaultProperties()); // add twice to ensure only included once

    final Section section = messageCard.getSections().get(0);
    final List<Fact> facts = section.getFacts();

    assertAll(
        () -> assertNull(messageCard.getTitle()),
        () -> assertEquals(title, messageCard.getSummary()),
        () -> assertEquals(SCHEMA, messageCard.getContext()),
        () -> assertNull(messageCard.getThemeColor()),
        () -> assertEquals(TYPE, messageCard.getType()),
        () -> assertNull(messageCard.getPotentialActions()),
        () -> assertEquals(1, messageCard.getSections().size()),
        () -> assertEquals(title, section.getActivityTitle()),
        () -> assertEquals(text, section.getActivitySubtitle()),
        () -> assertNull(section.getActivityImage()),
        () -> assertNull(section.getMarkdown()),
        () -> assertEquals(3, facts.size()),
        () -> assertTrue(facts.contains(new Fact("com.baloise.open.ms.teams.retries","3"))),
        () -> assertTrue(facts.contains(new Fact("com.baloise.open.ms.teams.retries.pause","60"))),
        () -> assertTrue(facts.contains(new Fact("com.baloise.open.ms.teams.webhook.uri","null")))
    );
  }

}

package com.baloise.open.ms.teams.templates;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdaptiveCardFactoryTest {
    private static final String SCHEMA = "http://adaptivecards.io/schemas/adaptive-card.json";
    private static final String TYPE = "AdaptiveCard";
    private static final String VERSION = "1.5";

    @Test
    void testSimpleMessageCreation() {
        final String title = "My Title to set";
        final String text = "And the content to be verified";

        final AdaptiveCard simpleAdaptiveCard = AdaptiveCardFactory.createSimpleAdaptiveCard(title, text);

        List<AdaptiveObject> body = simpleAdaptiveCard.getBody();
        assertEquals(2, body.size());

        assertInstanceOf(TextBlock.class, body.get(0));
        assertInstanceOf(TextBlock.class, body.get(1));

        TextBlock titleBlock = (TextBlock) body.get(0);
        TextBlock messageBlock = (TextBlock) body.get(1);

        assertAll(
                () -> assertEquals(SCHEMA, simpleAdaptiveCard.getSchema()),
                () -> assertEquals(TYPE, simpleAdaptiveCard.getType()),
                () -> assertEquals(VERSION, simpleAdaptiveCard.getVersion()),
                () -> assertNotNull(simpleAdaptiveCard.getTeamsCardProperties()),
                () -> assertEquals(TeamsCardProperties.TeamCardWidth.Full, simpleAdaptiveCard.getTeamsCardProperties().getWidth()),
                () -> assertEquals(title, titleBlock.getText()),
                () -> assertEquals(AdaptiveObject.Type.TEXT_BLOCK.getJsonValue(), titleBlock.getType()),
                () -> assertEquals(TextBlock.TextWeight.Bolder, titleBlock.getWeight()),
                () -> assertEquals(TextBlock.TextStyle.Heading, titleBlock.getStyle()),
                () -> assertEquals(AdaptiveObject.Type.TEXT_BLOCK.getJsonValue(), messageBlock.getType()),
                () -> assertEquals(text, messageBlock.getText())
        );
    }

    @Test
    void testSimpleMessageWithFactsCreation() {
        final String title = "My Title to set";
        final String text = "And the content to be verified";

        final AdaptiveCard simpleAdaptiveCard = AdaptiveCardFactory.createSimpleAdaptiveCard(title, text, Map.of("key1", "value1", "key2", "value2"));

        List<AdaptiveObject> body = simpleAdaptiveCard.getBody();
        assertEquals(3, body.size());

        assertInstanceOf(TextBlock.class, body.get(0));
        assertInstanceOf(TextBlock.class, body.get(1));
        assertInstanceOf(FactSet.class, body.get(2));

        TextBlock titleBlock = (TextBlock) body.get(0);
        TextBlock messageBlock = (TextBlock) body.get(1);
        FactSet factSet = (FactSet) body.get(2);
        List<Fact> facts = List.of(Fact.builder().title("key1").value("value1").build(), Fact.builder().title("key2").value("value2").build());

        assertAll(
                () -> assertEquals(SCHEMA, simpleAdaptiveCard.getSchema()),
                () -> assertEquals(TYPE, simpleAdaptiveCard.getType()),
                () -> assertEquals(VERSION, simpleAdaptiveCard.getVersion()),
                () -> assertNotNull(simpleAdaptiveCard.getTeamsCardProperties()),
                () -> assertEquals(TeamsCardProperties.TeamCardWidth.Full, simpleAdaptiveCard.getTeamsCardProperties().getWidth()),
                () -> assertEquals(title, titleBlock.getText()),
                () -> assertEquals(AdaptiveObject.Type.TEXT_BLOCK.getJsonValue(), titleBlock.getType()),
                () -> assertEquals(TextBlock.TextWeight.Bolder, titleBlock.getWeight()),
                () -> assertEquals(TextBlock.TextStyle.Heading, titleBlock.getStyle()),
                () -> assertEquals(AdaptiveObject.Type.TEXT_BLOCK.getJsonValue(), messageBlock.getType()),
                () -> assertEquals(text, messageBlock.getText()),
                () -> assertEquals(AdaptiveObject.Type.FACT_SET.getJsonValue(), factSet.getType()),
                () -> assertEquals(2, factSet.getFacts().size()),
                () -> assertTrue(factSet.getFacts().containsAll(facts))
        );
    }

    @Test
    void testBuilder() {
        final String title = "Whatever Title to use";
        final String text = "And even some content to be included";
        final String factKey = "fk1";
        final String factValue = "fact value to be set";

        final AdaptiveCard adaptiveCard = AdaptiveCardFactory.builder(title, text)
                .withFact(factKey, factValue)
                .withFact(null, "")
                .withFact(" ", null)
                .withFact(" A ", "B")
                .build();

        final TextBlock titleBlock = (TextBlock) adaptiveCard.getBody().get(0);
        final TextBlock textBlock = (TextBlock) adaptiveCard.getBody().get(1);
        final FactSet factSet = (FactSet) adaptiveCard.getBody().get(2);

        assertAll(
                () -> assertEquals(TYPE, adaptiveCard.getType()),
                () -> assertEquals(3, adaptiveCard.getBody().size()),
                () -> assertEquals(title, titleBlock.getText()),
                () -> assertEquals(TextBlock.TextStyle.Heading, titleBlock.getStyle()),
                () -> assertEquals(text, textBlock.getText()),
                () -> assertEquals(2, factSet.getFacts().size()),
                () -> assertTrue(factSet.getFacts().contains(new Fact(factKey, factValue))),
                () -> assertTrue(factSet.getFacts().contains(new Fact(" A ", "B")))
        );
    }
}
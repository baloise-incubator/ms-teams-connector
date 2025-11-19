package com.baloise.open.ms.teams.templates.card.adaptive;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.baloise.open.ms.teams.json.Serializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MentionTest extends PropertyReflectionTest {

    private final Mention testee = Mention.builder()
            .mentioned(MentionedPerson.builder().id("id").name("name").build())
            .text("text")
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(Mention.class, 3);
    }

    @Test
    void verifyDefaults() {
        assertEquals(AdaptiveObject.Type.MENTION.getJsonValue(), testee.getType());
        assertEquals(MentionedPerson.builder().id("id").name("name").build(), testee.getMentioned());
        assertEquals("text", testee.getText());
    }

    @Test
    void verifyFactory() {
        Mention mention = MentionFactory.createMention("id", "name lastname");
        assertEquals("mention", mention.getType());
        assertEquals(MentionedPerson.builder().id("id").name("name lastname").build(), mention.getMentioned());
        assertEquals("<at>name_lastname</at>", mention.getText());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"type\":\"mention\",\"mentioned\":{\"id\":\"id\",\"name\":\"name\"},\"text\":\"text\"}",
                Serializer.asJson(testee)
        );
    }
}

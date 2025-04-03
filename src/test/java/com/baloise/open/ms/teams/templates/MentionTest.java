package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MentionTest extends PropertyReflectionTest {

    private final Mention testee = Mention.builder()
            .mentioned(MentionedPerson.builder().id("id").name("name").build())
            .text("text")
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(Mention.class, 4);
    }

    @Test
    void verifyDefaults() {
        assertEquals(Mention.TYPE, testee.getType());
        assertEquals(MentionedPerson.builder().id("id").name("name").build(), testee.getMentioned());
        assertEquals("text", testee.getText());
    }

    @Test
    void verifyFactory() {
        Mention mention = MentionFactory.createMention("id", "name lastname");
        assertEquals(Mention.TYPE, mention.getType());
        assertEquals(MentionedPerson.builder().id("id").name("name lastname").build(), mention.getMentioned());
        assertEquals("<at>name_lastname</at>", mention.getText());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"type\":\"mention\",\"mentioned\":{\"id\":\"id\",\"name\":\"name\"},\"text\":\"text\"}",
                new GsonBuilder().create().toJson(testee)
        );
    }
}

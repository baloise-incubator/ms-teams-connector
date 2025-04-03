package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BadgeTest extends PropertyReflectionTest {

    private final Badge testee = Badge.builder()
            .text("text")
            .tooltip("tooltip")
            .shape(Badge.BadgeShape.Square)
            .appearance(Badge.BadgeAppearance.Tint)
            .style(Badge.BadgeStyle.Attention)
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(Badge.class, 7);
    }

    @Test
    void verifyDefaults() {
        assertEquals(Badge.TYPE, testee.getType());
        assertEquals("text", testee.getText());
        assertEquals("tooltip", testee.getTooltip());
        assertEquals(Badge.BadgeShape.Square, testee.getShape());
        assertEquals(Badge.BadgeAppearance.Tint, testee.getAppearance());
        assertEquals(Badge.BadgeStyle.Attention, testee.getStyle());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"type\":\"Badge\",\"text\":\"text\",\"tooltip\":\"tooltip\",\"shape\":\"Square\",\"appearance\":\"Tint\",\"style\":\"Attention\"}",
                new GsonBuilder().create().toJson(testee)
        );
    }
}

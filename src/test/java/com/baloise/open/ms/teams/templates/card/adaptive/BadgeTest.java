package com.baloise.open.ms.teams.templates.card.adaptive;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.baloise.open.ms.teams.json.Serializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BadgeTest extends PropertyReflectionTest {

    private final Badge testee = Badge.builder()
            .text("text")
            .tooltip("tooltip")
        .shape(Badge.BadgeShape.SQUARE)
            .appearance(Badge.BadgeAppearance.TINT)
            .style(Badge.BadgeStyle.ATTENTION)
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(Badge.class, 6);
    }

    @Test
    void verifyDefaults() {
        assertEquals(AdaptiveObject.Type.BADGE.getJsonValue(), testee.getType());
        assertEquals("text", testee.getText());
        assertEquals("tooltip", testee.getTooltip());
        assertEquals(Badge.BadgeShape.SQUARE, testee.getShape());
        assertEquals(Badge.BadgeAppearance.TINT, testee.getAppearance());
        assertEquals(Badge.BadgeStyle.ATTENTION, testee.getStyle());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"type\":\"Badge\",\"text\":\"text\",\"tooltip\":\"tooltip\",\"shape\":\"Square\",\"appearance\":\"Tint\",\"style\":\"Attention\"}",
            Serializer.asJson(testee)
        );
    }
}

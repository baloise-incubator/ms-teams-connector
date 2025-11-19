package com.baloise.open.ms.teams.templates.card.adaptive;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.baloise.open.ms.teams.json.Serializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockTest extends PropertyReflectionTest {

    private final TextBlock testee = TextBlock.builder()
            .text("text")
            .style(TextBlock.TextStyle.COLUMN_HEADER)
            .color(TextBlock.TextColor.WARNING)
            .size(TextBlock.TextSize.MEDIUM)
            .fontType(TextBlock.TextFontType.MONOSPACE)
            .wrap(true)
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(TextBlock.class, 8);
    }

    @Test
    void verifyDefaults() {
        assertEquals("TextBlock", testee.getType());
        assertEquals("text", testee.getText());
        assertEquals(TextBlock.TextStyle.COLUMN_HEADER, testee.getStyle());
        assertEquals(TextBlock.TextColor.WARNING, testee.getColor());
        assertEquals(TextBlock.TextSize.MEDIUM, testee.getSize());
        assertEquals(TextBlock.TextFontType.MONOSPACE, testee.getFontType());
        assertEquals(true, testee.getWrap());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"type\":\"TextBlock\",\"text\":\"text\",\"wrap\":true,\"style\":\"ColumnHeader\",\"size\":\"Medium\",\"fontType\":\"Monospace\",\"color\":\"Warning\"}",
                Serializer.asJson(testee)
        );
    }
}

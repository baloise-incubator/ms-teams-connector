package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockTest extends PropertyReflectionTest {

    private final TextBlock testee = TextBlock.builder()
            .text("text")
            .style(TextBlock.TextStyle.ColumnHeader)
            .color(TextBlock.TextColor.Warning)
            .size(TextBlock.TextSize.Medium)
            .fontType(TextBlock.TextFontType.Monospace)
            .wrap(true)
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(TextBlock.class, 9);
    }

    @Test
    void verifyDefaults() {
        assertEquals(TextBlock.TYPE, testee.getType());
        assertEquals("text", testee.getText());
        assertEquals(TextBlock.TextStyle.ColumnHeader, testee.getStyle());
        assertEquals(TextBlock.TextColor.Warning, testee.getColor());
        assertEquals(TextBlock.TextSize.Medium, testee.getSize());
        assertEquals(TextBlock.TextFontType.Monospace, testee.getFontType());
        assertEquals(true, testee.getWrap());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"type\":\"TextBlock\",\"text\":\"text\",\"wrap\":true,\"style\":\"ColumnHeader\",\"size\":\"Medium\",\"fontType\":\"Monospace\",\"color\":\"Warning\"}",
                new GsonBuilder().create().toJson(testee)
        );
    }
}

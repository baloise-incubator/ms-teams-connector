package com.baloise.open.ms.teams.templates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AdaptiveObjectTest {

  @Test
  void veriyTypeEnum() {
    final AdaptiveObject.Type[] values = AdaptiveObject.Type.values();
    assertEquals(8, values.length);
    assertAll(
        () -> assertEquals(AdaptiveObject.Type.CODE_BLOCK, AdaptiveObject.Type.valueOf("CODE_BLOCK")),
        () -> assertEquals("CodeBlock", AdaptiveObject.Type.CODE_BLOCK.getJsonValue()),
        () -> assertEquals(AdaptiveObject.Type.ADAPTIVE_CARD, AdaptiveObject.Type.valueOf("ADAPTIVE_CARD")),
        () -> assertEquals("AdaptiveCard", AdaptiveObject.Type.ADAPTIVE_CARD.getJsonValue()),
        () -> assertEquals(AdaptiveObject.Type.BADGE, AdaptiveObject.Type.valueOf("BADGE")),
        () -> assertEquals("Badge", AdaptiveObject.Type.BADGE.getJsonValue()),
        () -> assertEquals(AdaptiveObject.Type.CONTAINER, AdaptiveObject.Type.valueOf("CONTAINER")),
        () -> assertEquals("Container", AdaptiveObject.Type.CONTAINER.getJsonValue()),
        () -> assertEquals(AdaptiveObject.Type.FACT_SET, AdaptiveObject.Type.valueOf("FACT_SET")),
        () -> assertEquals("FactSet", AdaptiveObject.Type.FACT_SET.getJsonValue()),
        () -> assertEquals(AdaptiveObject.Type.CONTAINER_LAYOUT_FLOW, AdaptiveObject.Type.valueOf("CONTAINER_LAYOUT_FLOW")),
        () -> assertEquals("Layout.Flow", AdaptiveObject.Type.CONTAINER_LAYOUT_FLOW.getJsonValue()),
        () -> assertEquals(AdaptiveObject.Type.MENTION, AdaptiveObject.Type.valueOf("MENTION")),
        () -> assertEquals("mention", AdaptiveObject.Type.MENTION.getJsonValue()),
        () -> assertEquals(AdaptiveObject.Type.TEXT_BLOCK, AdaptiveObject.Type.valueOf("TEXT_BLOCK")),
        () -> assertEquals("TextBlock", AdaptiveObject.Type.TEXT_BLOCK.getJsonValue())
    );
  }
}
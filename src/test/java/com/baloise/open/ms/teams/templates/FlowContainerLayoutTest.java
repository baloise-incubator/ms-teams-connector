package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.json.Serializer;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlowContainerLayoutTest {

  @Test
  void verifySerialization() {
    var testee = new FlowContainerLayout.FlowContainerLayoutBuilder()
        .columnSpacing(Spacing.LARGE)
        .horizontalItemsAlignment(FlowContainerLayout.HorizontalItemsAlignment.CENTER)
        .verticalItemsAlignment(FlowContainerLayout.VerticalItemsAlignment.TOP)
        .rowSpacing(Spacing.MEDIUM)
        .columnSpacing(Spacing.PADDING)
        .itemFit(FlowContainerLayout.ItemFit.FILL)
        .itemWidth("20px")
        .minItemWidth("10px")
        .maxItemWidth("50px")
        .build();

    assertEquals(JsonParser.parseString("""
            {
              "type":"Layout.Flow",
              "horizontalItemsAlignment":"Center",
              "verticalItemsAlignment":"Top",
              "rowSpacing":"Medium",
              "columnSpacing":"Padding",
              "itemFit":"Fill",
              "itemWidth":"20px",
              "minItemWidth":"10px",
              "maxItemWidth":"50px"
            }
            """)
        , Serializer.asJsonTree(testee));
  }
}
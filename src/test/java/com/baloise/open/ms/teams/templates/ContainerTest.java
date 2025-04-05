package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.baloise.open.ms.teams.json.Serializer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContainerTest extends PropertyReflectionTest {

    private final Container testee = Container.builder()
            .style(Container.ContainerStyle.ATTENTION)
            .items(List.of(Badge.builder().build()))
            .layouts(List.of(FlowContainerLayout.builder().build()))
            .showBorder(true)
            .roundedCorners(true)
            .separator(true)
            .spacing(Spacing.LARGE)
            .id("id")
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(Container.class, 6);
    }

    @Test
    void verifyDefaults() {
        assertEquals("Container", testee.getType());
        assertEquals("id", testee.getId());
        assertEquals(List.of(Badge.builder().build()), testee.getItems());
        assertEquals(List.of(FlowContainerLayout.builder().build()), testee.getLayouts());
        assertEquals(true, testee.getShowBorder());
        assertEquals(true, testee.getRoundedCorners());
        assertEquals(true, testee.getSeparator());
        assertEquals(Spacing.LARGE, testee.getSpacing());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"type\":\"Container\",\"items\":[{\"type\":\"Badge\"}],\"layouts\":[{\"type\":\"Layout.Flow\"}],\"style\":\"Attention\",\"roundedCorners\":true,\"showBorder\":true,\"id\":\"id\",\"separator\":true,\"spacing\":\"Large\"}",
                Serializer.asJson(testee)
        );
    }
}

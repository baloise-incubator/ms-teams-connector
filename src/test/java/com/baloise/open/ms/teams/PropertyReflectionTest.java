package com.baloise.open.ms.teams;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class PropertyReflectionTest {

  final List<String> testPropertiesToIgnore = Arrays.asList("__$lineHits$__", "$jacocoData");

  protected void assertNumberOfProperties(Class<?> className, long expectedCount) {
    final Field[] declaredFields = className.getDeclaredFields();
    final long propertiesCount = Arrays.stream(declaredFields)
        .filter(field -> !testPropertiesToIgnore.contains(field.getName()))
        .count();
    assertEquals(expectedCount, propertiesCount);
  }

}

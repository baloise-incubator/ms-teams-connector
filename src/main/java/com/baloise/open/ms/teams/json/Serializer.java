/*
 * Copyright 2025 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baloise.open.ms.teams.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Serializer {

  private static final String PACKAGE_NAME = "com.baloise.open.ms.teams.templates.card.adaptive";
  private static final Class<JsonSerializableEnum> ASSIGNED_INTERFACE = JsonSerializableEnum.class;
  private static final EnumSerializer ENUM_SERIALIZER = new EnumSerializer();
  private static final Gson GSON = getConfiguredJson();

  private Serializer() {
    // static usage only
  }

  public static String asJson(final Object object) {
    return GSON.toJson(object);
  }

  public static JsonElement asJsonTree(final Object object) {
    return GSON.toJsonTree(object);
  }

  /**
   * Configures the Gson serializer. It searches for all types inside {@link #PACKAGE_NAME} implementing
   * the {@link #ASSIGNED_INTERFACE} and registers the {@link #ENUM_SERIALIZER} using jsonValue instead of name.
   */
  private static Gson getConfiguredJson() {
    return registerEnumTypes(new GsonBuilder()).create();
  }

  private static GsonBuilder registerEnumTypes(GsonBuilder gsonBuilder) {
    final File directory = new File("src/main/java/" + PACKAGE_NAME.replace(".", "/"));
    if (directory.exists() && directory.isDirectory()) {
      Arrays.stream(Objects.requireNonNull(directory.list()))
          .filter(file -> file.endsWith(".java"))
          .forEach(file -> processTemplateType(file, gsonBuilder));
    }
    return gsonBuilder;
  }

  private static void processTemplateType(final String file, final GsonBuilder gsonBuilder) {
    final String className = Serializer.PACKAGE_NAME + '.' + file.substring(0, file.length() - 5);
    try {
      final Class<?> clazz = Class.forName(className);
      processSingleType(gsonBuilder, clazz);
      for (Class<?> nestedClass : clazz.getDeclaredClasses()) {
        processSingleType(gsonBuilder, nestedClass);
      }
    } catch (ClassNotFoundException e) {
      // swallow exception
      log.warn(e.getLocalizedMessage());
    }
  }

  private static void processSingleType(final GsonBuilder gsonBuilder, final Class<?> type) {
    if (Serializer.ASSIGNED_INTERFACE.isAssignableFrom(type)) {
      log.debug("{} implements {}, register {}.", type, Serializer.ASSIGNED_INTERFACE, ENUM_SERIALIZER);
      gsonBuilder.registerTypeAdapter(type, ENUM_SERIALIZER);
    } else {
      log.debug("{} is not implementing {}.", type, Serializer.ASSIGNED_INTERFACE);
    }
  }

}


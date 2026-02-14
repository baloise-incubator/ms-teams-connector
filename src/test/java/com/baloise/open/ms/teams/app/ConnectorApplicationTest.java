package com.baloise.open.ms.teams.app;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ConnectorApplicationTest {

  HttpServer server;
  AtomicInteger calls;
  private EnvironmentVariables environmentVariables;

  @BeforeEach
  void setUp() throws IOException {
    environmentVariables = new EnvironmentVariables();
    calls = new AtomicInteger();
    server = HttpServer.create(new InetSocketAddress(0), 0);
    server.createContext("/webhook", exchange -> {
      calls.incrementAndGet();
      byte[] bytes = "ok".getBytes();
      exchange.sendResponseHeaders(200, bytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(bytes);
      }
    });
    server.start();
  }

  @AfterEach
  void tearDown() {
    if (environmentVariables != null) {
      try {
        environmentVariables.teardown();
      } catch (Exception ignored) {
        // Intentionally ignored: cleanup failures shouldn't fail tests; each test reinitializes env vars.
      }
    }
    server.stop(0);
  }

  @Test
  void publishWithEnvVarsShouldSucceed() {
    String url = "http://localhost:" + server.getAddress().getPort() + "/webhook";
    environmentVariables.set("WEBHOOK_URL", url);
    environmentVariables.set("MESSAGE_TITLE", "EnvTitle");
    environmentVariables.set("MESSAGE_TEXT", "EnvText");
    try {
      environmentVariables.setup();
    } catch (Exception e) {
      fail(e);
    }
    int code = ConnectorApplication.run();
    assertEquals(0, code);
    assertTrue(calls.get() >= 1);
  }

  @Test
  void missingWebhookEnvLeadsToValidationError() {
    environmentVariables.set("MESSAGE_TITLE", "T");
    environmentVariables.set("MESSAGE_TEXT", "Body");
    try {
      environmentVariables.setup();
    } catch (Exception e) {
      fail(e);
    }
    int code = ConnectorApplication.run();
    assertEquals(2, code);
  }

  @Test
  void missingTitleEnvLeadsToValidationError() {
    environmentVariables.set("WEBHOOK_URL", "url");
    environmentVariables.set("MESSAGE_TEXT", "Body");
    try {
      environmentVariables.setup();
    } catch (Exception e) {
      fail(e);
    }
    int code = ConnectorApplication.run();
    assertEquals(2, code);
  }

  @Test
  void missingTextEnvLeadsToValidationError() {
    environmentVariables.set("WEBHOOK_URL", "url");
    environmentVariables.set("MESSAGE_TITLE", "T");
    try {
      environmentVariables.setup();
    } catch (Exception e) {
      fail(e);
    }
    int code = ConnectorApplication.run();
    assertEquals(2, code);
  }

  @Test
  void blankWebhookEnvLeadsToValidationError() {
    environmentVariables.set("WEBHOOK_URL", "   ");
    environmentVariables.set("MESSAGE_TITLE", "T");
    environmentVariables.set("MESSAGE_TEXT", "Body");
    try {
      environmentVariables.setup();
    } catch (Exception e) {
      fail(e);
    }
    int code = ConnectorApplication.run();
    assertEquals(2, code);
  }

  @ParameterizedTest
  @CsvSource({
      "5,5",
      "  -42 ,-42",
      "0,0",
      "   ,null",
      "'',null",
      "abc,null",
      "123abc,null",
      "1.5,null"
  })
  void parseIntEnvHandlesValidAndInvalidInputs(String input, String expected) {
    Integer expectedValue;
    if (expected == null || expected.equals("null")) {
      expectedValue = null;
    } else {
      try {
        expectedValue = Integer.valueOf(expected.trim());
      } catch (NumberFormatException e) {
        expectedValue = null;
      }
    }
    assertEquals(expectedValue, ConnectorApplication.parseIntEnv(input));
  }

  @Test
  void parseIntEnvHandlesNullInput() {
    assertNull(ConnectorApplication.parseIntEnv(null));
  }
}

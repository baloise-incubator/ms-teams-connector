package com.baloise.open.ms.teams.templates;

import com.baloise.open.ms.teams.PropertyReflectionTest;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeBlockTest extends PropertyReflectionTest {

    private final CodeBlock testee = CodeBlock.builder()
            .codeSnippet("codeSnippet")
            .language("language")
            .startLineNumber(1)
            .build();

    @Test
    void verifyNumberOfProperties() {
        this.assertNumberOfProperties(CodeBlock.class, 5);
    }

    @Test
    void verifyDefaults() {
        assertEquals(CodeBlock.TYPE, testee.getType());
        assertEquals("codeSnippet", testee.getCodeSnippet());
        assertEquals("language", testee.getLanguage());
        assertEquals(1, testee.getStartLineNumber());
    }

    @Test
    void verifySerializaion() {
        assertEquals(
                "{\"type\":\"CodeBlock\",\"language\":\"language\",\"codeSnippet\":\"codeSnippet\",\"startLineNumber\":1}",
                new GsonBuilder().create().toJson(testee)
        );
    }
}

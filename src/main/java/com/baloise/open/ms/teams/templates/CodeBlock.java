package com.baloise.open.ms.teams.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>A formatted and syntax-colored code block.</p> *
 * <a href="https://adaptivecards.microsoft.com/?topic=CodeBlock" target="_blank">CodeBlock reference</a>
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CodeBlock extends AdaptiveObject {
    public static final String TYPE = "CodeBlock";

    private final String type = TYPE;
    private String language;
    private String codeSnippet;
    private Integer startLineNumber;
}

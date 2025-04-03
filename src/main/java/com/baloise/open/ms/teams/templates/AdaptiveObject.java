package com.baloise.open.ms.teams.templates;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@RequiredArgsConstructor
public abstract class AdaptiveObject {
    private String id;
    private Boolean separator;
    private Spacing spacing;
}

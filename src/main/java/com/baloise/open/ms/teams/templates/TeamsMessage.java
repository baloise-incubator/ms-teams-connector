/*
 * Copyright 2025 Baloise Group
 */
package com.baloise.open.ms.teams.templates;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TeamsMessage {
    @Builder.Default
    private final String type = "message";
    private String summary;
    @Builder.Default
    @SerializedName("attachments")
    private List<Attachment> attachments = new ArrayList<>();
}


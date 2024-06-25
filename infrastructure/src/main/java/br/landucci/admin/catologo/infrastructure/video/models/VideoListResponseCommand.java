package br.landucci.admin.catologo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record VideoListResponseCommand(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt
) {
}

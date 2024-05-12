package br.landucci.admin.catologo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoMessage(
        @JsonProperty("resource_id") String resourceId,
        @JsonProperty("file_path") String filePath
) {
}

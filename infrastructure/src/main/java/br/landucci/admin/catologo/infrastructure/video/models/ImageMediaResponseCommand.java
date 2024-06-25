package br.landucci.admin.catologo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageMediaResponseCommand(
        @JsonProperty("id") String id,
        @JsonProperty("checksum") String checksum,
        @JsonProperty("name") String name,
        @JsonProperty("location") String location
) {
}

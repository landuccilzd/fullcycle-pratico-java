package br.landucci.admin.catologo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateVideoResponseCommand(@JsonProperty("id") String id) {
}

package br.landucci.admin.catologo.infrastructure.castmember.model;

import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCastMemberRequestCommand(
        @JsonProperty("name") String name,
        @JsonProperty("type") CastMemberType type
) {

}

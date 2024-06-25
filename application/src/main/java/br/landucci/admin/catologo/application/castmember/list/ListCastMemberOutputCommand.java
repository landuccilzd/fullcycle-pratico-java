package br.landucci.admin.catologo.application.castmember.list;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;

import java.time.Instant;

public record ListCastMemberOutputCommand(String id, String name, CastMemberType type, Instant createdAt,
        Instant updatedAt) {

    public static ListCastMemberOutputCommand from(final CastMember castMember) {
        return new ListCastMemberOutputCommand(castMember.getId().getValue(), castMember.getName(),
                castMember.getType(), castMember.getCreatedAt(), castMember.getUpdatedAt());
    }
}

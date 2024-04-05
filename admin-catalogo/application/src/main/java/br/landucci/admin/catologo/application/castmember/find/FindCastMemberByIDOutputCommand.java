package br.landucci.admin.catologo.application.castmember.find;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;

import java.time.Instant;

public record FindCastMemberByIDOutputCommand(String id, String name, CastMemberType type,
                                              Instant createdAt, Instant updatedAt) {

    public static FindCastMemberByIDOutputCommand from(final CastMember castMember) {
        return new FindCastMemberByIDOutputCommand(castMember.getId().getValue(), castMember.getName(),
                castMember.getType(), castMember.getCreatedAt(), castMember.getUpdatedAt());
    }

}

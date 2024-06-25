package br.landucci.admin.catologo.application.castmember.update;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;

public record UpdateCastMemberInputCommand(String id, String name, CastMemberType type) {

    public static UpdateCastMemberInputCommand with(final String id, final String name, final CastMemberType type) {
        return new UpdateCastMemberInputCommand(id, name, type);
    }

    public static UpdateCastMemberInputCommand from(CastMember castMember) {
        return with(castMember.getId().getValue(), castMember.getName(), castMember.getType());
    }

}
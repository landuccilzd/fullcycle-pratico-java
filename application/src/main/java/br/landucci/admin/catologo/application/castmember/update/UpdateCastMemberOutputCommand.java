package br.landucci.admin.catologo.application.castmember.update;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;

public record UpdateCastMemberOutputCommand(String id) {

    public static UpdateCastMemberOutputCommand with(final String id) {
        return new UpdateCastMemberOutputCommand(id);
    }

    public static UpdateCastMemberOutputCommand from(final CastMemberID id) {
        return with(id.getValue());
    }

    public static UpdateCastMemberOutputCommand from(final CastMember castMember) {
        return from(castMember.getId());
    }
}

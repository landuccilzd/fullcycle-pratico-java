package br.landucci.admin.catologo.application.castmember.create;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;

public record CreateCastMemberOutputCommand(String id) {

    public static CreateCastMemberOutputCommand with(final String id) {
        return new CreateCastMemberOutputCommand(id);
    }

    public static CreateCastMemberOutputCommand from(final CastMemberID id) {
        return with(id.getValue());
    }

    public static CreateCastMemberOutputCommand from(final CastMember castMember) {
        return from(castMember.getId());
    }
}

package br.landucci.admin.catologo.application.castmember.delete;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;

public record DeleteCastMemberInputCommand(String id) {

    public static DeleteCastMemberInputCommand with(final String id) {
        return new DeleteCastMemberInputCommand(id);
    }

    public static DeleteCastMemberInputCommand from(final CastMemberID id) {
        return with(id.getValue());
    }

    public static DeleteCastMemberInputCommand from(final CastMember castMember) {
        return from(castMember.getId());
    }
}

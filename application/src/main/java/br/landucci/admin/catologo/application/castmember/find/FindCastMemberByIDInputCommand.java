package br.landucci.admin.catologo.application.castmember.find;

import br.landucci.admin.catologo.domain.castmember.CastMemberID;

public record FindCastMemberByIDInputCommand(String id) {

    public static FindCastMemberByIDInputCommand with(final String id) {
        return new FindCastMemberByIDInputCommand(id);
    }

    public static FindCastMemberByIDInputCommand from(final CastMemberID id) {
        return with(id.getValue());
    }
}

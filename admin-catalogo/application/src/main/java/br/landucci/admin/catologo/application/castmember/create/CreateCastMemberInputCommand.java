package br.landucci.admin.catologo.application.castmember.create;

import br.landucci.admin.catologo.domain.castmember.CastMemberType;

public record CreateCastMemberInputCommand(String name, CastMemberType type) {

    public static CreateCastMemberInputCommand with(final String name, final CastMemberType type) {
        return new CreateCastMemberInputCommand(name, type);
    }
}

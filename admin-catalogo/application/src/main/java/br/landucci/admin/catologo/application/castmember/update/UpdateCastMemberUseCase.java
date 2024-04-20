package br.landucci.admin.catologo.application.castmember.update;

import br.landucci.admin.catologo.application.UseCase;

public abstract sealed class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberInputCommand, UpdateCastMemberOutputCommand>
        permits DefaultUpdateCastMemberUseCase {
}

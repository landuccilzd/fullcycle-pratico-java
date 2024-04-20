package br.landucci.admin.catologo.application.castmember.delete;

import br.landucci.admin.catologo.application.UnitUseCase;

public abstract sealed class DeleteCastMemberUseCase
        extends UnitUseCase<DeleteCastMemberInputCommand>
        permits DefaultDeleteCastMemberUseCase {
}

package br.landucci.admin.catologo.application.castmember.delete;

import br.landucci.admin.catologo.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<DeleteCastMemberInputCommand>
        permits DefaultDeleteCastMemberUseCase {
}

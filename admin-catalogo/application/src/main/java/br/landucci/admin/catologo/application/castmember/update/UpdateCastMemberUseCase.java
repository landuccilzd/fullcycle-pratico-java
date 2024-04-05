package br.landucci.admin.catologo.application.castmember.update;

import br.landucci.admin.catologo.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberInputCommand, UpdateCastMemberOutputCommand>
        permits DefaultUpdateCastMemberUseCase {
}

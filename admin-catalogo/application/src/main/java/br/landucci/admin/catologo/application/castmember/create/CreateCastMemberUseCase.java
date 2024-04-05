package br.landucci.admin.catologo.application.castmember.create;

import br.landucci.admin.catologo.application.UseCase;

public sealed abstract class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberInputCommand, CreateCastMemberOutputCommand>
        permits DefaultCreateCastMemberUseCase {}
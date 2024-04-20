package br.landucci.admin.catologo.application.castmember.create;

import br.landucci.admin.catologo.application.UseCase;

public abstract sealed class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberInputCommand, CreateCastMemberOutputCommand>
        permits DefaultCreateCastMemberUseCase {}
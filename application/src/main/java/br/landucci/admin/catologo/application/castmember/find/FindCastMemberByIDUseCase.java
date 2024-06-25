package br.landucci.admin.catologo.application.castmember.find;

import br.landucci.admin.catologo.application.UseCase;

public abstract sealed class FindCastMemberByIDUseCase
        extends UseCase<FindCastMemberByIDInputCommand, FindCastMemberByIDOutputCommand>
        permits DefaultFindCastMemberByIDUseCase {
}

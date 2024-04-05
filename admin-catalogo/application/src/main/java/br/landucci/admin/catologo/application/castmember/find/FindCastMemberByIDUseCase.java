package br.landucci.admin.catologo.application.castmember.find;

import br.landucci.admin.catologo.application.UseCase;

public sealed abstract class FindCastMemberByIDUseCase
        extends UseCase<FindCastMemberByIDInputCommand, FindCastMemberByIDOutputCommand>
        permits DefaultFindCastMemberByIDUseCase {
}

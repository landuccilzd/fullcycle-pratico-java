package br.landucci.admin.catologo.application.castmember.list;

import br.landucci.admin.catologo.application.UseCase;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;

public abstract sealed class ListCastMemberUseCase
        extends UseCase<SearchQuery, Pagination<ListCastMemberOutputCommand>>
        permits DefaultListCastMemberUseCase {
}

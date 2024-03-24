package br.landucci.admin.catologo.application.genre.list;


import br.landucci.admin.catologo.application.UseCase;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<ListGenreOutputCommand>> {
}

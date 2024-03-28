package br.landucci.admin.catologo.application.category.list;

import br.landucci.admin.catologo.application.UseCase;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.domain.pagination.Pagination;

public abstract class ListCategoryUseCase extends
        UseCase<SearchQuery, Pagination<ListCategoriesOutputCommand>> {
}

package br.landucci.admin.catologo.application.category.list;

import br.landucci.admin.catologo.application.UseCase;
import br.landucci.admin.catologo.domain.category.CategorySearchQuery;
import br.landucci.admin.catologo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<ListCategoriesOutputCommand>> {
}

package br.landucci.admin.catologo.application.category.list;

import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoryUseCase extends ListCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultListCategoryUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<ListCategoriesOutputCommand> execute(SearchQuery query) {
        return this.gateway.findAll(query)
                .map(ListCategoriesOutputCommand::from);
    }

}
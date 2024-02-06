package br.landucci.admin.catologo.application.category.list;

import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private final CategoryGateway gateway;

    public DefaultListCategoriesUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<ListCategoriesOutputCommand> execute(SearchQuery query) {
        return this.gateway.findAll(query)
                .map(ListCategoriesOutputCommand::from);
    }

}
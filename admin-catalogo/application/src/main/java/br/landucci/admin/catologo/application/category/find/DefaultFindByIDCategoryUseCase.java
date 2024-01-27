package br.landucci.admin.catologo.application.category.find;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultFindByIDCategoryUseCase extends FindByIDCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultFindByIDCategoryUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public FindByIDCategoryOutputCommand execute(FindByIDCategoryInputCommand input) {
        final var id = CategoryID.from(input.id());

        return this.gateway.findById(id)
                .map(FindByIDCategoryOutputCommand::from)
                .orElseThrow(notFound(id));
    }

    private Supplier<NotFoundException> notFound(final CategoryID id) {
        return () -> NotFoundException.with(Category.class, id);
    }
}

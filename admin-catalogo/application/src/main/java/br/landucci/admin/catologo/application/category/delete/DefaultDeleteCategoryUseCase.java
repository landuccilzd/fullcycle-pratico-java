package br.landucci.admin.catologo.application.category.delete;

import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(DeleteCategoryInputCommand input) {
        final var id = CategoryID.from(input.id());
        final var category = this.gateway.findById(id).orElseThrow(notFound(id));
        this.gateway.deleteById(id);
    }

    private Supplier<DomainException> notFound(final CategoryID id) {
        return () -> DomainException.with(
                new ValidationError("Category with ID %s was not found".formatted(id.getValue()))
        );
    }

}

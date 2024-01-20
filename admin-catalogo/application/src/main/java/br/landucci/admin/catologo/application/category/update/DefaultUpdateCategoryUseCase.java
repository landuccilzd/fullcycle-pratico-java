package br.landucci.admin.catologo.application.category.update;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutputCommand> execute(final UpdateCategoryInputCommand input) {
        final var id = CategoryID.from(input.id());
        final var name = input.name();
        final var description = input.description();
        final var active = input.active();

        final var category = this.gateway.findById(id).orElseThrow(notFound(id));

        final var notification = Notification.create();
        category.updateName(name).updateDescription(description).validate(notification);

        if (!active) {
            category.deactivate();
        } else {
            category.activate();
        }

        return notification.hasErrors() ? API.Left(notification) : update(category);
    }

    private Supplier<DomainException> notFound(final CategoryID id) {
        return () -> DomainException.with(
                new ValidationError("Category with ID %s was not found".formatted(id.getValue()))
        );
    }

    private Either<Notification,UpdateCategoryOutputCommand> update(final Category category) {
        return API.Try(() -> this.gateway.update(category)).toEither()
                .bimap(Notification::create, UpdateCategoryOutputCommand::from);
    }
}

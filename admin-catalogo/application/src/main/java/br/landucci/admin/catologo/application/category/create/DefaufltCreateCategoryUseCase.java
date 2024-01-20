package br.landucci.admin.catologo.application.category.create;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaufltCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaufltCreateCategoryUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Either<Notification,CreateCategoryOutputCommand> execute(final CreateCategoryInputCommand input) {
        final var name = input.name();
        final var description = input.description();
        final var active = input.active();

        final var notification = Notification.create();
        final var category = Category.newCategory(name, description, active);
        category.validate(notification);

        return notification.hasErrors() ? API.Left(notification) : create(category);
    }

    private Either<Notification,CreateCategoryOutputCommand> create(final Category category) {
        return API.Try(() -> this.gateway.create(category)).toEither()
                .bimap(Notification::create, CreateCategoryOutputCommand::from);
    }
}

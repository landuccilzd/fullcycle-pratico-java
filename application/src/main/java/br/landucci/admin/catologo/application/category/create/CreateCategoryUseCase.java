package br.landucci.admin.catologo.application.category.create;

import br.landucci.admin.catologo.application.UseCase;
import br.landucci.admin.catologo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends
        UseCase<CreateCategoryInputCommand, Either<Notification, CreateCategoryOutputCommand>> {
}

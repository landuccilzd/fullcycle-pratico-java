package br.landucci.admin.catologo.application.category.update;

import br.landucci.admin.catologo.application.UseCase;
import br.landucci.admin.catologo.application.category.create.CreateCategoryInputCommand;
import br.landucci.admin.catologo.application.category.create.CreateCategoryOutputCommand;
import br.landucci.admin.catologo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends
        UseCase<UpdateCategoryInputCommand, Either<Notification, UpdateCategoryOutputCommand>> {
}

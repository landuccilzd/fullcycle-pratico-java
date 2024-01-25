package br.landucci.admin.catologo.infrastructure.api.controllers;

import br.landucci.admin.catologo.application.category.create.CreateCategoryInputCommand;
import br.landucci.admin.catologo.application.category.create.CreateCategoryOutputCommand;
import br.landucci.admin.catologo.application.category.create.CreateCategoryUseCase;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.validation.handler.Notification;
import br.landucci.admin.catologo.infrastructure.api.CategoryAPI;
import br.landucci.admin.catologo.infrastructure.category.models.CreateCategoryApiInputCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
    }

    @Override
    public Pagination<?> list(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInputCommand input) {
        final var inputCommand = CreateCategoryInputCommand.with(
                input.name(), input.description(), input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
            ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutputCommand, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(inputCommand).fold(onError, onSuccess);
    }


}
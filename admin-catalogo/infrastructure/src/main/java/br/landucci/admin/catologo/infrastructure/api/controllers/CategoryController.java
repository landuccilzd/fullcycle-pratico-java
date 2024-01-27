package br.landucci.admin.catologo.infrastructure.api.controllers;

import br.landucci.admin.catologo.application.category.create.CreateCategoryInputCommand;
import br.landucci.admin.catologo.application.category.create.CreateCategoryOutputCommand;
import br.landucci.admin.catologo.application.category.create.CreateCategoryUseCase;
import br.landucci.admin.catologo.application.category.delete.DeleteCategoryInputCommand;
import br.landucci.admin.catologo.application.category.delete.DeleteCategoryUseCase;
import br.landucci.admin.catologo.application.category.find.FindByIDCategoryInputCommand;
import br.landucci.admin.catologo.application.category.find.FindByIDCategoryUseCase;
import br.landucci.admin.catologo.application.category.list.ListCategoriesUseCase;
import br.landucci.admin.catologo.application.category.update.UpdateCategoryInputCommand;
import br.landucci.admin.catologo.application.category.update.UpdateCategoryOutputCommand;
import br.landucci.admin.catologo.application.category.update.UpdateCategoryUseCase;
import br.landucci.admin.catologo.domain.category.CategorySearchQuery;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.validation.handler.Notification;
import br.landucci.admin.catologo.infrastructure.api.CategoryAPI;
import br.landucci.admin.catologo.infrastructure.category.models.CreateCategoryRequestCommand;
import br.landucci.admin.catologo.infrastructure.category.models.FindByIdCategoryResponseCommand;
import br.landucci.admin.catologo.infrastructure.category.models.ListCategoriesResponseCommand;
import br.landucci.admin.catologo.infrastructure.category.models.UpdateCategoryRequestCommand;
import br.landucci.admin.catologo.infrastructure.category.presentetrs.CategoryAPIPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final ListCategoriesUseCase listCategoryUseCase;
    private final FindByIDCategoryUseCase findByIDCategoryUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(final ListCategoriesUseCase listCategoryUseCase,
                              final FindByIDCategoryUseCase findByIDCategoryUseCase,
                              final CreateCategoryUseCase createCategoryUseCase,
                              final UpdateCategoryUseCase updateCategoryUseCase,
                              final DeleteCategoryUseCase deleteCategoryUseCase) {
        this.listCategoryUseCase = Objects.requireNonNull(listCategoryUseCase);
        this.findByIDCategoryUseCase = Objects.requireNonNull(findByIDCategoryUseCase);
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    }

    @Override
    public Pagination<ListCategoriesResponseCommand> list(String search, int page, int perPage, String sort, String direction) {
        final var query = new CategorySearchQuery(page, perPage, search, sort, direction);
        return listCategoryUseCase.execute(query).map(CategoryAPIPresenter::present);
    }

    @Override
    public FindByIdCategoryResponseCommand find(final String id) {
        final var input = FindByIDCategoryInputCommand.with(id);
        final var output = this.findByIDCategoryUseCase.execute(input);
        return CategoryAPIPresenter.present(output);
    }

    @Override
    public ResponseEntity<?> create(final CreateCategoryRequestCommand command) {
        final var input = CreateCategoryInputCommand.with(
                command.name(), command.description(), command.active() != null ? command.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutputCommand, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(input).fold(onError, onSuccess);
    }

    @Override
    public ResponseEntity<?> update(String id, UpdateCategoryRequestCommand command) {
        final var input = UpdateCategoryInputCommand.with(id, command.name(), command.description(), command.active());

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutputCommand, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.ok(output);

        return this.updateCategoryUseCase.execute(input).fold(onError, onSuccess);
    }

    @Override
    public void delete(String id) {
        final var input = DeleteCategoryInputCommand.with(id);
        this.deleteCategoryUseCase.execute(input);
    }

}
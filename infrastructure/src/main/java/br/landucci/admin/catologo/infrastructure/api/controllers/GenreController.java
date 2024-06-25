package br.landucci.admin.catologo.infrastructure.api.controllers;

import br.landucci.admin.catologo.application.genre.create.CreateGenreInputCommand;
import br.landucci.admin.catologo.application.genre.create.CreateGenreUseCase;
import br.landucci.admin.catologo.application.genre.delete.DeleteGenreInputCommand;
import br.landucci.admin.catologo.application.genre.delete.DeleteGenreUseCase;
import br.landucci.admin.catologo.application.genre.find.FindByIDGenreInputCommand;
import br.landucci.admin.catologo.application.genre.find.FindGenreByIDUseCase;
import br.landucci.admin.catologo.application.genre.list.ListGenreUseCase;
import br.landucci.admin.catologo.application.genre.update.UpdateGenreInputCommand;
import br.landucci.admin.catologo.application.genre.update.UpdateGenreUseCase;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.infrastructure.api.GenreAPI;
import br.landucci.admin.catologo.infrastructure.genre.models.CreateGenreRequestCommand;
import br.landucci.admin.catologo.infrastructure.genre.models.FindGenreByIdResponseCommand;
import br.landucci.admin.catologo.infrastructure.genre.models.ListGenresResponseCommand;
import br.landucci.admin.catologo.infrastructure.genre.models.UpdateGenreRequestCommand;
import br.landucci.admin.catologo.infrastructure.genre.presentetrs.GenreAPIPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {
    private final ListGenreUseCase listGenreUseCase;
    private final FindGenreByIDUseCase findGenreByIDUseCase;
    private final CreateGenreUseCase createGenreUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;

    public GenreController(final ListGenreUseCase listGenreUseCase, final FindGenreByIDUseCase findGenreByIDUseCase,
            final CreateGenreUseCase createGenreUseCase, final UpdateGenreUseCase updateGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase) {
        this.listGenreUseCase = listGenreUseCase;
        this.findGenreByIDUseCase = findGenreByIDUseCase;
        this.createGenreUseCase = createGenreUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
    }

    @Override
    public Pagination<ListGenresResponseCommand> list(final String search, final int page, final int perPage,
            final String sort, final String direction) {
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return this.listGenreUseCase.execute(query).map(GenreAPIPresenter::present);
    }

    @Override
    public FindGenreByIdResponseCommand find(String id) {
        final var input = FindByIDGenreInputCommand.with(id);
        final var output = this.findGenreByIDUseCase.execute(input);
        return GenreAPIPresenter.present(output);
    }

    @Override
    public ResponseEntity<?> create(CreateGenreRequestCommand command) {
        final var input = CreateGenreInputCommand.with(command.name(), command.isActive(), command.categories());
        final var output = this.createGenreUseCase.execute(input);
        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> update(String id, UpdateGenreRequestCommand command) {
        final var input = UpdateGenreInputCommand.with(id, command.name(), command.isActive(), command.categories());
        final var output = this.updateGenreUseCase.execute(input);
        return ResponseEntity.ok().body(output);
    }

    @Override
    public void delete(String id) {
        final var input = DeleteGenreInputCommand.with(id);
        this.deleteGenreUseCase.execute(input);
    }
}

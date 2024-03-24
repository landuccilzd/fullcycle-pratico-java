package br.landucci.admin.catologo.application.genre.list;

import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListGenreUseCase extends ListGenreUseCase {

    private final GenreGateway genreGateway;

    public DefaultListGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<ListGenreOutputCommand> execute(final SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery).map(ListGenreOutputCommand::from);
    }

}
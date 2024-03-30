package br.landucci.admin.catologo.application.genre.find;

import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.genre.GenreID;

import java.util.Objects;

public class DefaultFindGenreByIDUseCase extends FindGenreByIDUseCase {

    private final GenreGateway genreGateway;

    public DefaultFindGenreByIDUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public FindByIdGenreOutputCommand execute(final FindByIDGenreInputCommand command) {
        final var aGenreId = GenreID.from(command.id());
        return this.genreGateway.findById(aGenreId)
                .map(FindByIdGenreOutputCommand::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, aGenreId));
    }

}
package br.landucci.admin.catologo.application.genre;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.application.genre.delete.DeleteGenreInputCommand;
import br.landucci.admin.catologo.application.genre.delete.DeleteGenreUseCase;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreGateway gateway;

    @Autowired
    private GenreRepository repository;

    @Test
    void givenAValidGenreId_whenDeletingAGenre_shouldDeleteGenre() {
        final var genre = gateway.create(Genre.newGenre("Ação", true));
        final var expectedId = genre.getId();

        Assertions.assertEquals(1, repository.count());

        final var input = DeleteGenreInputCommand.with(expectedId.getValue());
        Assertions.assertDoesNotThrow(() -> useCase.execute(input));

        Assertions.assertEquals(0, repository.count());
    }

    @Test
    void givenAnInvalidGenreId_whenDeletingAGenre_shouldReturnInvalidArgumentException() {
        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        gateway.create(Genre.newGenre("Ação", true));
        Assertions.assertEquals(1, repository.count());

        final var input = DeleteGenreInputCommand.with(expectedId.getValue());
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(input);
        });

        Assertions.assertEquals(1, repository.count());

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }
}

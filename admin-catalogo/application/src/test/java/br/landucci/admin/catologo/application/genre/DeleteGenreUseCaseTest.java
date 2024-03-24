package br.landucci.admin.catologo.application.genre;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.category.delete.DeleteCategoryInputCommand;
import br.landucci.admin.catologo.application.genre.delete.DefaultDeleteGenreUseCase;
import br.landucci.admin.catologo.application.genre.delete.DeleteGenreInputCommand;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        // given
        final var aGenre = Genre.newGenre("Ação", true);
        final var expectedId = aGenre.getId();

        doNothing().when(genreGateway).deleteById(any());

        final var input = DeleteGenreInputCommand.with(expectedId.getValue());
        Assertions.assertDoesNotThrow(() -> useCase.execute(input));
        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var expectedId = GenreID.from("123");

        doNothing().when(genreGateway).deleteById(any());

        final var input = DeleteGenreInputCommand.with(expectedId.getValue());
        Assertions.assertDoesNotThrow(() -> useCase.execute(input));
        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        final var aGenre = Genre.newGenre("Ação", true);
        final var expectedId = aGenre.getId();

        doThrow(new IllegalStateException("Gateway error")).when(genreGateway).deleteById(any());

        final var input = DeleteGenreInputCommand.with(expectedId.getValue());
        Assertions.assertThrows(IllegalStateException.class, () -> {
            useCase.execute(input);
        });
        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
    }
}

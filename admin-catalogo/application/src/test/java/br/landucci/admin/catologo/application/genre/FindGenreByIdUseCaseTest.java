package br.landucci.admin.catologo.application.genre;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.genre.find.DefaultFindGenreByIDUseCase;
import br.landucci.admin.catologo.application.genre.find.FindByIDGenreInputCommand;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class FindGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultFindGenreByIDUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );
        final var genre = Genre.newGenre(expectedName, expectedIsActive).adicionarCategorias(expectedCategories);
        final var expectedId = genre.getId();

        when(genreGateway.findById(any())).thenReturn(Optional.of(genre));

        final var input = FindByIDGenreInputCommand.with(expectedId.getValue());
        final var actualGenre = useCase.execute(input);

        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), actualGenre.deletedAt());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        when(genreGateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> {
            final var input = FindByIDGenreInputCommand.with(expectedId.getValue());
            useCase.execute(input);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}

package br.landucci.admin.catologo.application.genre;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.genre.update.DefaultUpdateGenreUseCase;
import br.landucci.admin.catologo.application.genre.update.UpdateGenreInputCommand;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

class UpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;
    @Mock
    private GenreGateway gateway;
    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenUpdatingAGenre_thenShouldReturnACreatedGenre() {
        final var genre = Genre.newGenre("Genero", true);
        final var expectedId = genre.getId();
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreInputCommand.with(expectedId.getValue(), expectedName,
                expectedActive, asString(expectedCategories));

        Mockito.when(gateway.findById(expectedId)).thenReturn(Optional.of(Genre.clone(genre)));
        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(gateway, Mockito.times(1)).update(Mockito.argThat(updatedGenre -> {
            return Objects.equals(expectedId, updatedGenre.getId()) &&
                    Objects.equals(expectedName, updatedGenre.getName()) &&
                    Objects.equals(expectedActive, updatedGenre.isActive()) &&
                    Objects.equals(expectedCategories, updatedGenre.getCategories()) &&
                    Objects.equals(genre.getCreatedAt(), updatedGenre.getCreatedAt()) &&
                    genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt()) &&
                    Objects.isNull(updatedGenre.getDeletedAt());
        }));
    }

    @Test
    void givenAValidCommandWithCategories_whenUpdatingAGenre_thenShouldReturnACreatedGenre() {
        // given
        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aCommand = UpdateGenreInputCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.clone(aGenre)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(expectedCategories);

        Mockito.when(gateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(gateway, Mockito.times(1)).findById(expectedId);

        Mockito.verify(categoryGateway, Mockito.times(1))
                .existsByIds(expectedCategories);

//        Mockito.verify(gateway, Mockito.times(1)).update(ArgumentMatchers.argThat(aUpdatedGenre ->
//                Objects.equals(expectedId, aUpdatedGenre.getId())
//                        && Objects.equals(expectedName, aUpdatedGenre.getName())
//                        && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
//                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
//                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
//                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
//                        && Objects.isNull(aUpdatedGenre.getDeletedAt())
//        ));
    }

    @Test
    void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreInputCommand.with(expectedId.getValue(), expectedName, expectedIsActive,
                asString(expectedCategories));

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.clone(genre)));
        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        Assertions.assertTrue(genre.isActive());
        Assertions.assertNull(genre.getDeletedAt());

        // when
        final var output = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());

        Mockito.verify(gateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(gateway, Mockito.times(1)).update(ArgumentMatchers.argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(genre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && genre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                        && Objects.nonNull(aUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        // given
        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        final var aCommand = UpdateGenreInputCommand.with(expectedId.getValue(), expectedName, expectedIsActive,
                asString(expectedCategories));

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.clone(genre)));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(gateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        final var filmes = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.getId();
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);

        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories were not found: 456, 789";
        final var expectedErrorMessageTwo = "Name should not be null";

        final var aCommand = UpdateGenreInputCommand.with(expectedId.getValue(), null, expectedIsActive,
                asString(expectedCategories));

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.clone(genre)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(filmes));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        Mockito.verify(gateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway, categoryGateway);
    }

}
package br.landucci.admin.catologo.application.genre;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.genre.create.CreateGenreInputCommand;
import br.landucci.admin.catologo.application.genre.create.DefaultCreateGenreUseCase;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

class CreateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;
    @Mock
    private GenreGateway gateway;
    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCreateAGenre_thenShouldReturnACreatedGenre() {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = CreateGenreInputCommand.with(expectedName, expectedActive, asString(expectedCategories));

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.argThat(genre -> {
            return Objects.nonNull(genre.getId()) &&
                    Objects.equals(expectedName, genre.getName()) &&
                    Objects.equals(expectedActive, genre.isActive()) &&
                    Objects.equals(expectedCategories, genre.getCategories()) &&
                    Objects.nonNull(genre.getCreatedAt()) &&
                    Objects.nonNull(genre.getUpdatedAt()) &&
                    Objects.isNull(genre.getDeletedAt());
        }));
    }

    @Test
    void givenAValidCommandWithCategoires_whenCreateAGenre_thenShouldReturnACreatedGenre() {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456"),
                CategoryID.from("789")
        );

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(expectedCategories);

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = CreateGenreInputCommand.with(expectedName, expectedActive, asString(expectedCategories));

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);

        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.argThat(genre -> {
            return Objects.nonNull(genre.getId()) &&
                    Objects.equals(expectedName, genre.getName()) &&
                    Objects.equals(expectedActive, genre.isActive()) &&
                    Objects.equals(expectedCategories, genre.getCategories()) &&
                    Objects.nonNull(genre.getCreatedAt()) &&
                    Objects.nonNull(genre.getUpdatedAt()) &&
                    Objects.isNull(genre.getDeletedAt());
        }));
    }

    @Test
    void givenAnInValidNullNameCommand_whenCreateAGenre_thenShouldReturnDomainException() {
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "Name should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateGenreInputCommand.with(null, expectedActive, asString(expectedCategories));
        final var exception = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInValidEmptyNameCommand_whenCreateAGenre_thenShouldReturnDomainException() {
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "Name should not be empty";
        final var expectedErrorCount = 1;

        final var command = CreateGenreInputCommand.with("", expectedActive, asString(expectedCategories));
        final var exception = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidCommandWithCategoriesNotExisting_whenCratingAGenre_thenShouldReturnNotificationException() {
        final var filmes = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);

        final var expectedErrorMessage = "Some categories were not found: %s, %s"
                .formatted(series.getValue(), documentarios.getValue());

        final var expectedErrorCount = 1;

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(filmes));

        final var command = CreateGenreInputCommand.with(expectedName, expectedActive, asString(expectedCategories));
        final var exception = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidCommandWithNullNameAndCategoriesNotExisting_whenCratingAGenre_thenShouldReturnNotificationException() {
        final var filmes = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");
        final var expectedActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);
        final var expectedErrorCount = 2;
        final var expectedErrorMessageName = "Name should not be null";
        final var expectedErrorMessageCategories = "Some categories were not found: %s, %s"
                .formatted(series.getValue(), documentarios.getValue());


        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(filmes));

        final var command = CreateGenreInputCommand.with(null, expectedActive, asString(expectedCategories));
        final var exception = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageCategories, exception.firstError().message());
        Assertions.assertEquals(expectedErrorMessageName, exception.getErrors().get(1).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway, categoryGateway);
    }
}

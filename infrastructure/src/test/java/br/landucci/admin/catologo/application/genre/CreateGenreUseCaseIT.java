package br.landucci.admin.catologo.application.genre;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.application.genre.create.CreateGenreInputCommand;
import br.landucci.admin.catologo.application.genre.create.CreateGenreUseCase;
import br.landucci.admin.catologo.domain.Identifier;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

@IntegrationTest
class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;
    @SpyBean
    private GenreGateway gateway;
    @SpyBean
    private CategoryGateway categoryGateway;
    @Autowired
    private GenreRepository repository;

    @Test
    void givenAValidCommand_whenCreateAGenre_thenShouldReturnACreatedGenre() {
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategories = List.of(filmes.getId());

        final var command = CreateGenreInputCommand.with(expectedName, expectedActive, asString(expectedCategories));
        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var genre = this.repository.findById(output.id()).get();
        Assertions.assertNotNull(genre);
        Assertions.assertEquals(output.id(), genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedActive, genre.isActive());
        Assertions.assertEquals(expectedCategories.size(), genre.categoriesCount());
        Assertions.assertTrue(expectedCategories.containsAll(genre.getCategoriesAsList()));
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertNotNull(genre.getUpdatedAt());
        Assertions.assertNull(genre.getDeletedAt());
    }

    @Test
    void givenAValidCommandWithoutCategories_whenCreatingGenre_shouldReturnGenreId() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreInputCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var genre = this.repository.findById(output.id()).get();

        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedIsActive, genre.isActive());
        Assertions.assertEquals(expectedCategories.size(), genre.categoriesCount());
        Assertions.assertTrue(expectedCategories.containsAll(genre.getCategoriesAsList()));
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertNotNull(genre.getUpdatedAt());
        Assertions.assertNull(genre.getDeletedAt());
    }

    @Test
    void givenAValidCommandWithInactiveGenre_whenCreatingGenre_shouldReturnGenreId() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreInputCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var genre = this.repository.findById(output.id()).get();

        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedIsActive, genre.isActive());
        Assertions.assertEquals(expectedCategories.size(), genre.categoriesCount());
        Assertions.assertTrue(expectedCategories.containsAll(genre.getCategoriesAsList()));
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertNotNull(genre.getUpdatedAt());
        Assertions.assertNotNull(genre.getDeletedAt());
    }

    @Test
    void givenAInvalidEmptyName_whenCreatingAGenre_shouldReturnDomainException() {
        final var expectedName = "";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "Name should not be empty";
        final var expectedErrorCount = 1;

        final var command = CreateGenreInputCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var exception = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        Mockito.verify(this.categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(this.gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAInvalidNullNameCommand_whenCreatingAGenre_shouldReturnDomainException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "Name should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateGenreInputCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var exception = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        Mockito.verify(this.categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(this.gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var series =  this.categoryGateway.create(Category.newCategory("Séries", null, true));
        final var filmes = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");
        final var expectName = "";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes, series.getId(), documentarios);
        final var expectedErrorMessageOne = "Some categories were not found: 456, 789";
        final var expectedErrorMessageTwo = "Name should not be empty";
        final var expectedErrorCount = 2;

        final var command = CreateGenreInputCommand.with(expectName, expectedIsActive, asString(expectedCategories));
        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, exception.getErrors().get(1).message());

        Mockito.verify(this.categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
        Mockito.verify(this.gateway, Mockito.times(0)).create(Mockito.any());
    }

    protected List<String> asString(final List<? extends Identifier> ids) {
        return ids.stream().map(Identifier::getValue).toList();
    }
}
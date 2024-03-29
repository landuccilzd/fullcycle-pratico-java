package br.landucci.admin.catologo.application.genre;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.application.genre.update.UpdateGenreInputCommand;
import br.landucci.admin.catologo.application.genre.update.UpdateGenreUseCase;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@IntegrationTest
public class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase useCase;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private GenreGateway gateway;

    @Autowired
    private GenreRepository repository;

    @Test
    public void givenAValidCommand_whenUpdatingGenre_shouldReturnGenreId() {
        final var genre = gateway.create(Genre.newGenre("acao", true));
        final var expectedId = genre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var input = UpdateGenreInputCommand.with(expectedId.getValue(), expectedName, expectedIsActive,
                asString(expectedCategories)
        );

        final var output = this.useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());

        final var updatedGenre = this.repository.findById(genre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, updatedGenre.getName());
        Assertions.assertEquals(expectedIsActive, updatedGenre.isActive());
        Assertions.assertEquals(expectedCategories.size(), updatedGenre.categoriesCount());
        Assertions.assertTrue(expectedCategories.containsAll(updatedGenre.getCategoriesAsList()));
        Assertions.assertEquals(genre.getCreatedAt(), updatedGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt()));
        Assertions.assertNull(updatedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithCategories_whenUpdatingGenre_shouldReturnGenreId() {
        final var filmes = this.categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = this.categoryGateway.create(Category.newCategory("Séries", null, true));
        final var genre = this.gateway.create(Genre.newGenre("acao", true));
        final var expectedId = genre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var input = UpdateGenreInputCommand.with(expectedId.getValue(), expectedName, expectedIsActive,
                asString(expectedCategories)
        );

        final var output = this.useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());

        final var updatedGenre = this.repository.findById(genre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, updatedGenre.getName());
        Assertions.assertEquals(expectedIsActive, updatedGenre.isActive());
//        Assertions.assertEquals(expectedCategories.size(), updatedGenre.categoriesCount());
        Assertions.assertTrue(expectedCategories.containsAll(updatedGenre.getCategoriesAsList()));
        Assertions.assertEquals(genre.getCreatedAt(), updatedGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt()));
        Assertions.assertNull(updatedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenUpdatingGenre_shouldReturnGenreId() {
        final var genre = this.gateway.create(Genre.newGenre("acao", true));
        final var expectedId = genre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var input = UpdateGenreInputCommand.with(expectedId.getValue(), expectedName, expectedIsActive,
                asString(expectedCategories)
        );

        Assertions.assertTrue(genre.isActive());
        Assertions.assertNull(genre.getDeletedAt());

        final var output = this.useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());

        final var updatedGenre = this.repository.findById(genre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, updatedGenre.getName());
        Assertions.assertEquals(expectedIsActive, updatedGenre.isActive());
        Assertions.assertEquals(expectedCategories.size(), updatedGenre.categoriesCount());
        Assertions.assertTrue(expectedCategories.containsAll(updatedGenre.getCategoriesAsList()));
        Assertions.assertEquals(genre.getCreatedAt(), updatedGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt()));
        Assertions.assertNotNull(updatedGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidName_whenUpdatingAGenre_shouldReturnNotificationException() {
        // given
        final var aGenre = gateway.create(Genre.newGenre("acao", true));
        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        final var input = UpdateGenreInputCommand.with(expectedId.getValue(), expectedName, expectedIsActive,
                asString(expectedCategories)
        );

        final var exception = Assertions.assertThrows(NotificationException.class, () -> this.useCase.execute(input));

        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(this.gateway, times(1)).findById(eq(expectedId));
        Mockito.verify(this.categoryGateway, times(0)).existsByIds(any());
        Mockito.verify(this.gateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidName_whenUpdatingGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        final var filmes = this.categoryGateway.create(Category.newCategory("Filems", null, true));
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series, documentarios);
        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories were not found: 456, 789";
        final var expectedErrorMessageTwo = "Name should not be null";

        final var genre = this.gateway.create(Genre.newGenre("acao", true));
        final var expectedId = genre.getId();

        final var input = UpdateGenreInputCommand.with(expectedId.getValue(), expectedName, expectedIsActive,
                asString(expectedCategories)
        );

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> this.useCase.execute(input)
        );

        Assertions.assertEquals(expectedErrorCount, actualException.errorCount());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.firstError().message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        Mockito.verify(this.gateway, times(1)).findById(eq(expectedId));
        Mockito.verify(this.categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        Mockito.verify(this.gateway, times(0)).update(any());
    }

    private List<String> asString(final List<CategoryID> ids) {
        return ids.stream().map(CategoryID::getValue).toList();
    }

}
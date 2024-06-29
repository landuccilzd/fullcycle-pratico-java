package br.landucci.admin.catologo.application.genre;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.application.genre.find.FindByIDGenreInputCommand;
import br.landucci.admin.catologo.application.genre.find.FindGenreByIDUseCase;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
class FindGenreByIdUseCaseIT {

    @Autowired
    private FindGenreByIDUseCase useCase;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway gateway;

    @Test
    void givenAValidId_whenFindingGenre_shouldReturnGenre() {
        final var series = this.categoryGateway.create(Category.newCategory("Séries", null, true));
        final var filmes = this.categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(series.getId(), filmes.getId());

        final var genre = gateway.create(
                Genre.newGenre(expectedName, expectedIsActive).adicionarCategorias(expectedCategories)
        );
        final var expectedId = genre.getId();

        final var input = FindByIDGenreInputCommand.with(expectedId.getValue());
        final var foundGenre = useCase.execute(input);

        Assertions.assertEquals(expectedId.getValue(), foundGenre.id());
        Assertions.assertEquals(expectedName, foundGenre.name());
        Assertions.assertEquals(expectedIsActive, foundGenre.isActive());
        Assertions.assertEquals(expectedCategories.size(), foundGenre.categoriesCount());
        Assertions.assertTrue(asString(expectedCategories).containsAll(foundGenre.categories()));
        Assertions.assertEquals(genre.getCreatedAt(), foundGenre.createdAt());
//        Assertions.assertTrue(genre.getUpdatedAt().isBefore(foundGenre.updatedAt()));
        Assertions.assertEquals(genre.getDeletedAt(), foundGenre.deletedAt());
    }

    @Test
    void givenAValidId_whenFindingAGenreAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        final var input = FindByIDGenreInputCommand.with(expectedId.getValue());
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(input));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private List<String> asString(final List<CategoryID> ids) {
        return ids.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}

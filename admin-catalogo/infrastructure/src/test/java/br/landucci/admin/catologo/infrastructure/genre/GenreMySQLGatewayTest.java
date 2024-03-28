package br.landucci.admin.catologo.infrastructure.genre;

import br.landucci.admin.catologo.MySQLGatewayTest;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.infrastructure.category.CategoryMySQLGateway;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreJpaEntity;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {
    @Autowired
    private CategoryMySQLGateway categoryGateway;
    @Autowired
    private GenreMySQLGateway gateway;
    @Autowired
    private GenreRepository repository;

    @Test
    public void givenAPersistedGenre_whenFindingById_thenShouldReturnTheFoundGenre() {
        final var category = categoryGateway.create(
                Category.newCategory("Filmes", "Todos os tipos de filmes", true));
        final var expectedName = "Ficção Científica";
        final var expectedActive = true;
        final var expectedCategories = List.of(category.getId());
        final var expectedCategoryCount = 1;

        final var genre = Genre.newGenre(expectedName, expectedActive);
        genre.adicionarCategorias(expectedCategories);

        repository.saveAndFlush(GenreJpaEntity.from(genre));
        Assertions.assertEquals(1, repository.count());

        final var oGenre = gateway.findById(genre.getId());
        Assertions.assertTrue(oGenre.isPresent());

        final var foundGenre = oGenre.get();
        Assertions.assertEquals(genre.getId().getValue(), foundGenre.getId().getValue());
        Assertions.assertEquals(expectedName, foundGenre.getName());
        Assertions.assertEquals(expectedActive, foundGenre.isActive());
        Assertions.assertEquals(expectedCategories, foundGenre.getCategories());
        Assertions.assertEquals(genre.getCreatedAt(), foundGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(foundGenre.getUpdatedAt()));
        Assertions.assertEquals(genre.getDeletedAt(), foundGenre.getDeletedAt());

        Assertions.assertEquals(expectedCategoryCount, foundGenre.categoriesCount());
        Assertions.assertEquals(category.getId(), foundGenre.getCategories().get(0));
    }

    @Test
    public void givenExistingGenres_whenListing_thenShouldReturnAListOfGenre() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 6;
        mockGenres();
        Assertions.assertEquals(expectedTotal, repository.count());

        final var query = new SearchQuery(0, 10, "", "name", "asc");
        final var foundGenres = this.gateway.findAll(query);

        Assertions.assertEquals(expectedPage, foundGenres.currentPage());
        Assertions.assertEquals(expectedPerPage, foundGenres.perPage());
        Assertions.assertEquals(expectedTotal, foundGenres.total());
        Assertions.assertEquals(expectedTotal, foundGenres.items().size());
    }

    @Test
    public void givenNonExistingCategories_whenListing_thenShouldReturnAnEmptyList() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 0;

        Assertions.assertEquals(expectedTotal, repository.count());

        final var query = new SearchQuery(0, 10, "", "name", "asc");
        final var foundGenres = this.gateway.findAll(query);

        Assertions.assertNotNull(foundGenres);

        Assertions.assertEquals(expectedPage, foundGenres.currentPage());
        Assertions.assertEquals(expectedPerPage, foundGenres.perPage());
        Assertions.assertEquals(expectedTotal, foundGenres.total());
    }

    @ParameterizedTest
    @CsvSource({
            "ani, 0, 10, 1, 1, Animação, name, asc",
            "com, 0, 10, 1, 1, Comédia, name, asc",
            "dra, 0, 10, 1, 1, Drama, name, asc",
            "fic, 0, 10, 1, 1, Ficção Científica, name, asc",
            "sus, 0, 10, 1, 1, Suspense, name, asc",
            "ter, 0, 10, 1, 1, Terror, name, asc"
    })
    public void givenFollowPagination_whenFindingAllOnPage1_thenShouldReturnPaginated(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedGenreName,
            final String expectedSortBy,
            final String expectedSortDir
    ) {
        Assertions.assertEquals(0, this.repository.count());
        mockGenres();
        Assertions.assertEquals(6, this.repository.count());

        var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSortBy, expectedSortDir);
        var foundGenres = this.gateway.findAll(query);

        Assertions.assertEquals(expectedPage, foundGenres.currentPage());
        Assertions.assertEquals(expectedPerPage, foundGenres.perPage());
        Assertions.assertEquals(expectedTotal, foundGenres.total());
        Assertions.assertEquals(expectedItemsCount, foundGenres.items().size());
        Assertions.assertEquals(expectedGenreName, foundGenres.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            ", 0, 1, 1, 6, Animação, name, asc",
            ", 1, 1, 1, 6, Comédia, name, asc",
            ", 2, 1, 1, 6, Drama, name, asc",
            ", 3, 1, 1, 6, Ficção Científica, name, asc",
            ", 4, 1, 1, 6, Suspense, name, asc",
            ", 5, 1, 1, 6, Terror, name, asc"
    })
    public void givenExistingCategories_whenFilteringName_thenShouldReturnAFilteredListOfCategories(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedGenreName,
            final String expectedSortBy,
            final String expectedSortDir
    ) {
        Assertions.assertEquals(0, repository.count());
        mockGenres();
        Assertions.assertEquals(expectedTotal, repository.count());

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSortBy, expectedSortDir);
        final var foundGenres = this.gateway.findAll(query);

        Assertions.assertEquals(expectedPage, foundGenres.currentPage());
        Assertions.assertEquals(expectedPerPage, foundGenres.perPage());
        Assertions.assertEquals(expectedTotal, foundGenres.total());
        Assertions.assertEquals(expectedItemsCount, foundGenres.items().size());
        Assertions.assertEquals(expectedGenreName, foundGenres.items().get(0).getName());
    }

    @Test
    public void givenANonPersistedGenre_whenFindingByAnInexistingId_thenShouldNotReturnTheGenre() {
        Assertions.assertEquals(0, repository.count());
        final var oGenre = gateway.findById(GenreID.from("123"));
        Assertions.assertTrue(oGenre.isEmpty());
    }

    @Test
    public void givenAValidGenre_whenCreating_thenShouldPersistAGenre() {
        final var category = categoryGateway.create(
                Category.newCategory("Filmes", "Todos os tipos de filmes", true));
        final var expectedName = "Ficção Científica";
        final var expectedActive = true;
        final var expectedCategories = List.of(category.getId());
        final var expectedCategoryCount = 1;

        final var genre = Genre.newGenre(expectedName, expectedActive);
        genre.adicionarCategorias(expectedCategories);

        final var createdGenre = gateway.create(genre);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(genre.getId(), createdGenre.getId());
        Assertions.assertEquals(expectedName, createdGenre.getName());
        Assertions.assertEquals(expectedActive, createdGenre.isActive());
        Assertions.assertEquals(expectedCategories, createdGenre.getCategories());
        Assertions.assertEquals(genre.getCreatedAt(), createdGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(createdGenre.getUpdatedAt()));
        Assertions.assertEquals(genre.getDeletedAt(), createdGenre.getDeletedAt());

        Assertions.assertEquals(expectedCategoryCount, createdGenre.categoriesCount());
        Assertions.assertEquals(category.getId(), createdGenre.getCategories().get(0));

        final var oGenre = repository.findById(genre.getId().getValue());
        Assertions.assertTrue(oGenre.isPresent());

        final var foundGenre = oGenre.get();
        Assertions.assertEquals(genre.getId().getValue(), foundGenre.getId());
        Assertions.assertEquals(expectedName, foundGenre.getName());
        Assertions.assertEquals(expectedActive, foundGenre.isActive());
        Assertions.assertEquals(expectedCategories, foundGenre.getCategoriesAsList());
        Assertions.assertEquals(genre.getCreatedAt(), foundGenre.getCreatedAt());
        Assertions.assertEquals(genre.getUpdatedAt(), foundGenre.getUpdatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), foundGenre.getDeletedAt());

        Assertions.assertEquals(expectedCategoryCount, foundGenre.categoriesCount());
        Assertions.assertEquals(category.getId(), foundGenre.getCategoriesAsList().get(0));
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCreating_thenShouldPersistAGenre() {
        final var expectedName = "Ficção Científica";
        final var expectedActive = true;
        final var expectedCategoryCount = 0;

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var createdGenre = gateway.create(genre);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(genre.getId(), createdGenre.getId());
        Assertions.assertEquals(expectedName, createdGenre.getName());
        Assertions.assertEquals(expectedActive, createdGenre.isActive());
        Assertions.assertTrue(createdGenre.getCategories().isEmpty());
        Assertions.assertEquals(genre.getCreatedAt(), createdGenre.getCreatedAt());
        Assertions.assertEquals(genre.getUpdatedAt(), createdGenre.getUpdatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), createdGenre.getDeletedAt());
        Assertions.assertEquals(expectedCategoryCount, createdGenre.categoriesCount());

        final var oGenre = repository.findById(genre.getId().getValue());
        Assertions.assertTrue(oGenre.isPresent());

        final var foundGenre = oGenre.get();
        Assertions.assertEquals(genre.getId().getValue(), foundGenre.getId());
        Assertions.assertEquals(expectedName, foundGenre.getName());
        Assertions.assertEquals(expectedActive, foundGenre.isActive());
        Assertions.assertTrue(foundGenre.getCategoriesAsList().isEmpty());
        Assertions.assertEquals(genre.getCreatedAt(), foundGenre.getCreatedAt());
        Assertions.assertEquals(genre.getUpdatedAt(), foundGenre.getUpdatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), foundGenre.getDeletedAt());
        Assertions.assertEquals(expectedCategoryCount, foundGenre.categoriesCount());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenUpdating_thenShouldPersistAnUpdatedGenre() {
        final var expectedName = "Ficção Científica";
        final var expectedActive = true;
        final var expectedCategoryCount = 1;

        final var genre = Genre.newGenre("Ação", expectedActive);
        final var createdGenre = gateway.create(genre);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(genre.getId(), createdGenre.getId());
        Assertions.assertEquals("Ação", createdGenre.getName());
        Assertions.assertEquals(expectedActive, createdGenre.isActive());
        Assertions.assertTrue(createdGenre.getCategories().isEmpty());
        Assertions.assertEquals(genre.getCreatedAt(), createdGenre.getCreatedAt());
        Assertions.assertEquals(genre.getUpdatedAt(), createdGenre.getUpdatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), createdGenre.getDeletedAt());
        Assertions.assertEquals(0, createdGenre.categoriesCount());

        final var foundGenre = repository.findById(genre.getId().getValue()).get().toAggregate();
        foundGenre.updateName(expectedName);
        final var category = categoryGateway.create(
                Category.newCategory("Filmes", "Todos os tipos de filmes", true));
        final var expectedCategories = List.of(category.getId());
        foundGenre.adicionarCategorias(expectedCategories);

        final var updatedGenre = gateway.create(foundGenre);

        Assertions.assertEquals(foundGenre.getId().getValue(), updatedGenre.getId().getValue());
        Assertions.assertEquals(foundGenre.getName(), updatedGenre.getName());
        Assertions.assertEquals(foundGenre.isActive(), updatedGenre.isActive());
        Assertions.assertEquals(foundGenre.getCreatedAt(), updatedGenre.getCreatedAt());
        Assertions.assertTrue(foundGenre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt()));
        Assertions.assertEquals(foundGenre.getDeletedAt(), updatedGenre.getDeletedAt());
        Assertions.assertEquals(expectedCategoryCount, updatedGenre.categoriesCount());
        Assertions.assertEquals(expectedCategories, updatedGenre.getCategories());
    }

    @Test
    public void givenPersistedGenre_whenDeleting_thenShouldRemoveAGenre() {
        final var expectedName = "Ficção Científica";
        final var expectedActive = true;
        final var expectedCategoryCount = 0;

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var createdGenre = gateway.create(genre);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(genre.getId(), createdGenre.getId());
        Assertions.assertEquals(expectedName, createdGenre.getName());
        Assertions.assertEquals(expectedActive, createdGenre.isActive());
        Assertions.assertTrue(createdGenre.getCategories().isEmpty());
        Assertions.assertEquals(genre.getCreatedAt(), createdGenre.getCreatedAt());
        Assertions.assertEquals(genre.getUpdatedAt(), createdGenre.getUpdatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), createdGenre.getDeletedAt());
        Assertions.assertEquals(0, createdGenre.categoriesCount());

        gateway.deleteById(genre.getId());
        Assertions.assertEquals(expectedCategoryCount, repository.count());
    }

    private void mockGenres() {
        this.repository.saveAllAndFlush(List.of(
                GenreJpaEntity.from(Genre.newGenre("Comédia", true)),
                GenreJpaEntity.from(Genre.newGenre("Suspense", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true)),
                GenreJpaEntity.from(Genre.newGenre("Ficção Científica", true)),
                GenreJpaEntity.from(Genre.newGenre("Animação", true))
        ));
    }
}

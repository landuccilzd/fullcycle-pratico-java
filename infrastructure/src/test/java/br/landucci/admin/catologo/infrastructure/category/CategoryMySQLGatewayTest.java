package br.landucci.admin.catologo.infrastructure.category;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.MySQLGatewayTest;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryJpaEntity;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway gateway;
    @Autowired
    private CategoryRepository repository;

    @Test
    void givenAValidCategory_whenCreating_thenShouldReturnANewCategory() {
        final var expectedName = "Ficção Científia";
        final var expectedDescription = "Filmes de ficção Científia";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);

        Assertions.assertEquals(0, repository.count());

        final var newCategory = gateway.create(category);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(category.getId(), newCategory.getId());
        Assertions.assertEquals(expectedName, newCategory.getName());
        Assertions.assertEquals(expectedDescription, newCategory.getDescription());
        Assertions.assertEquals(expectedActive, newCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), newCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), newCategory.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), newCategory.getDeletedAt());
        Assertions.assertNull(newCategory.getDeletedAt());

        final var entity = repository.findById(category.getId().getValue()).get();

        Assertions.assertEquals(category.getId().getValue(), entity.getId());
        Assertions.assertEquals(expectedName, entity.getName());
        Assertions.assertEquals(expectedDescription, entity.getDescription());
        Assertions.assertEquals(expectedActive, entity.isActive());
        Assertions.assertEquals(category.getCreatedAt(), entity.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), entity.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), entity.getDeletedAt());
        Assertions.assertNull(entity.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenUpdating_thenShouldReturnAnUpdatedCategory() {
        final var expectedName = "Ficção Científia";
        final var expectedDescription = "Filmes de ficção Científia";
        final var expectedActive = true;
        final var category = Category.newCategory("Comédia", "Filmes de comédia", true);

        Assertions.assertEquals(0, repository.count());

        final var entity = CategoryJpaEntity.from(category);
        this.repository.saveAndFlush(entity);

        Assertions.assertEquals(1, repository.count());

        final var categoryUpdate = Category.clone(category);
        categoryUpdate.updateName(expectedName).updateDescription(expectedDescription).activate();
        final var updatedCategory = this.gateway.update(categoryUpdate);

        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(category.getId(), updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedActive, updatedCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), updatedCategory.getCreatedAt());
//        Assertions.assertTrue(category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt()));
        Assertions.assertEquals(category.getDeletedAt(), updatedCategory.getDeletedAt());
        Assertions.assertNull(updatedCategory.getDeletedAt());

        final var updatedEntity = repository.findById(category.getId().getValue()).get();

        Assertions.assertEquals(category.getId().getValue(), updatedEntity.getId());
        Assertions.assertEquals(expectedName, updatedEntity.getName());
        Assertions.assertEquals(expectedDescription, updatedEntity.getDescription());
        Assertions.assertEquals(expectedActive, updatedEntity.isActive());
        Assertions.assertEquals(category.getCreatedAt(), updatedEntity.getCreatedAt());
//        Assertions.assertTrue(category.getUpdatedAt().isBefore(updatedEntity.getUpdatedAt()));
        Assertions.assertEquals(category.getDeletedAt(), updatedEntity.getDeletedAt());
        Assertions.assertNull(updatedEntity.getDeletedAt());
    }

    @Test
    void givenAnExistingCategory_whenDeleting_thenShouldDeleteCategory() {
        final var category = Category.newCategory("Comédia", "Filmes de comédia", true);

        Assertions.assertEquals(0, repository.count());

        final var entity = CategoryJpaEntity.from(category);
        this.repository.saveAndFlush(entity);

        Assertions.assertEquals(1, repository.count());

        this.gateway.deleteById(category.getId());

        Assertions.assertEquals(0, repository.count());
    }

    @Test
    void givenAnExistingCategory_whenDeletingAnInvalidId_thenShouldReturnAnError() {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        Assertions.assertEquals(0, repository.count());

        final var id = CategoryID.from("123");
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.gateway.deleteById(id));

        Assertions.assertEquals(0, repository.count());
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void givenAnExistingCategory_whenFindingById_thenShouldReturnTheFoundCategory() {
        final var expectedName = "Ficção Científia";
        final var expectedDescription = "Filmes de ficção Científia";
        final var expectedActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);

        Assertions.assertEquals(0, repository.count());

        final var entity = CategoryJpaEntity.from(category);
        this.repository.saveAndFlush(entity);

        Assertions.assertEquals(1, repository.count());

        final var foundCategory = this.gateway.findById(category.getId()).get();

        Assertions.assertEquals(category.getId(), foundCategory.getId());
        Assertions.assertEquals(expectedName, foundCategory.getName());
        Assertions.assertEquals(expectedDescription, foundCategory.getDescription());
        Assertions.assertEquals(expectedActive, foundCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), foundCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), foundCategory.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), foundCategory.getDeletedAt());
        Assertions.assertNull(foundCategory.getDeletedAt());
    }

    @Test
    void givenAnNonExistingCategory_whenFindingAnInvalidId_thenShouldReturnEmpty() {
        final var optional = this.gateway.findById(CategoryID.from("123"));
        Assertions.assertTrue(optional.isEmpty());
    }

    @Test
    void givenExistingCategories_whenListing_thenShouldReturnAListOfCategories() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var categoryFiccao = Category.newCategory("Ficção Científica", "Filmes de ficção científica", true);
        final var categoryComedia = Category.newCategory("Comédia", "Filmes de comédia", true);
        final var categorySuspense = Category.newCategory("Suspense", "Filmes de suspense", true);

        Assertions.assertEquals(0, repository.count());

        this.repository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(categoryFiccao),
                CategoryJpaEntity.from(categoryComedia),
                CategoryJpaEntity.from(categorySuspense)
        ));

        Assertions.assertEquals(3, repository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");
        final var foundCategories = this.gateway.findAll(query);

        Assertions.assertEquals(expectedPage, foundCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, foundCategories.perPage());
        Assertions.assertEquals(expectedTotal, foundCategories.total());
        Assertions.assertEquals(expectedPerPage, foundCategories.items().size());
        Assertions.assertEquals(categoryComedia.getId(), foundCategories.items().get(0).getId());
    }

    @Test
    void givenNonExistingCategories_whenListing_thenShouldReturnAnEmptyList() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, repository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");
        final var foundCategories = this.gateway.findAll(query);

        Assertions.assertEquals(expectedPage, foundCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, foundCategories.perPage());
        Assertions.assertEquals(expectedTotal, foundCategories.total());
    }

    @Test
    void givenFollowPagination_whenFindingAllOnPage1_thenShouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var categoryFiccao = Category.newCategory("Ficção Científica", "Filmes de ficção científica", true);
        final var categoryComedia = Category.newCategory("Comédia", "Filmes de comédia", true);
        final var categorySuspense = Category.newCategory("Suspense", "Filmes de suspense", true);

        Assertions.assertEquals(0, repository.count());

        this.repository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(categoryFiccao),
                CategoryJpaEntity.from(categoryComedia),
                CategoryJpaEntity.from(categorySuspense)
        ));

        Assertions.assertEquals(3, repository.count());

        var query = new SearchQuery(0, 1, "", "name", "asc");
        var foundCategories = this.gateway.findAll(query);

        Assertions.assertEquals(expectedPage, foundCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, foundCategories.perPage());
        Assertions.assertEquals(expectedTotal, foundCategories.total());
        Assertions.assertEquals(expectedPerPage, foundCategories.items().size());
        Assertions.assertEquals(categoryComedia.getId(), foundCategories.items().get(0).getId());

        expectedPage = 1;
        query = new SearchQuery(1, 1, "", "name", "asc");
        foundCategories = this.gateway.findAll(query);

        Assertions.assertEquals(expectedPage, foundCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, foundCategories.perPage());
        Assertions.assertEquals(expectedTotal, foundCategories.total());
        Assertions.assertEquals(expectedPerPage, foundCategories.items().size());
        Assertions.assertEquals(categoryFiccao.getId(), foundCategories.items().get(0).getId());

        expectedPage = 2;
        query = new SearchQuery(2, 1, "", "name", "asc");
        foundCategories = this.gateway.findAll(query);

        Assertions.assertEquals(expectedPage, foundCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, foundCategories.perPage());
        Assertions.assertEquals(expectedTotal, foundCategories.total());
        Assertions.assertEquals(expectedPerPage, foundCategories.items().size());
        Assertions.assertEquals(categorySuspense.getId(), foundCategories.items().get(0).getId());
    }

    @Test
    void givenExistingCategories_whenFilteringName_thenShouldReturnAFilteredListOfCategories() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var categoryFiccao = Category.newCategory("Ficção Científica", "Filmes de ficção científica", true);
        final var categoryComedia = Category.newCategory("Comédia", "Filmes de comédia", true);
        final var categorySuspense = Category.newCategory("Suspense", "Filmes de suspense", true);

        Assertions.assertEquals(0, repository.count());

        this.repository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(categoryFiccao),
                CategoryJpaEntity.from(categoryComedia),
                CategoryJpaEntity.from(categorySuspense)
        ));

        Assertions.assertEquals(3, repository.count());

        final var query = new SearchQuery(0, 1, "com", "name", "asc");
        final var foundCategories = this.gateway.findAll(query);

        Assertions.assertEquals(expectedPage, foundCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, foundCategories.perPage());
        Assertions.assertEquals(expectedTotal, foundCategories.total());
        Assertions.assertEquals(expectedPerPage, foundCategories.items().size());
        Assertions.assertEquals(categoryComedia.getId(), foundCategories.items().get(0).getId());
    }

    @Test
    void givenExistingCategories_whenFilteringDescription_thenShouldReturnAFilteredListOfCategories() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var categoryFiccao = Category.newCategory("Ficção Científica", "Filmes de ficção científica", true);
        final var categoryComedia = Category.newCategory("Comédia", "Filmes de comédia", true);
        final var categorySuspense = Category.newCategory("Suspense", "Filmes de suspense", true);

        Assertions.assertEquals(0, repository.count());

        this.repository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(categoryFiccao),
                CategoryJpaEntity.from(categoryComedia),
                CategoryJpaEntity.from(categorySuspense)
        ));

        Assertions.assertEquals(3, repository.count());

        final var query = new SearchQuery(0, 1, "suspense", "name", "asc");
        final var foundCategories = this.gateway.findAll(query);

        Assertions.assertEquals(expectedPage, foundCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, foundCategories.perPage());
        Assertions.assertEquals(expectedTotal, foundCategories.total());
        Assertions.assertEquals(expectedPerPage, foundCategories.items().size());
        Assertions.assertEquals(categorySuspense.getId(), foundCategories.items().get(0).getId());
    }

    @Test
    void givenSomeExistingCategories_whenCallsExistsByIds_shouldReturnIds() {
        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var series = Category.newCategory("Séries", "Uma categoria assistida", true);
        final var documentarios = Category.newCategory("Documentários", "A categoria menos assistida", true);

        Assertions.assertEquals(0, repository.count());

        repository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, repository.count());

        final var expectedIds = List.of(filmes.getId(), series.getId());
        final var ids = List.of(filmes.getId(), series.getId(), CategoryID.from("123"));

        final var result = gateway.existsByIds(ids);

        Assertions.assertEquals(expectedIds.size(), result.size());
        Assertions.assertTrue(expectedIds.containsAll(result));
    }
}
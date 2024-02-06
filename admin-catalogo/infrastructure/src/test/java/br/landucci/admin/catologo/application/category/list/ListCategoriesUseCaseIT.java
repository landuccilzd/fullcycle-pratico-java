package br.landucci.admin.catologo.application.category.list;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryJpaEntity;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;
    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenAValidInput_whenListingCategories_thenShouldReturnAListOfCategories() {
        final var categories = List.of(
                Category.newCategory("Ficção científica", "Filmes de ficção científica", true),
                Category.newCategory("Comédia", "Filmes de comédia", true),
                Category.newCategory("Aventura", "Filmes de aventura", true),
                Category.newCategory("Romance", "Filmes de romance", true),
                Category.newCategory("Suspense", "Filmes de suspense", true),
                Category.newCategory("Drama", "Filmes de drama", true),
                Category.newCategory("Terror", "Filmes de terror", true)
        ).stream().map(CategoryJpaEntity::from).toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 7;
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        Assertions.assertEquals(0, this.repository.count());

        this.repository.saveAllAndFlush(categories);

        Assertions.assertEquals(7, this.repository.count());

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var result = useCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, result.items().size());
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedItemsCount, result.total());
    }

}
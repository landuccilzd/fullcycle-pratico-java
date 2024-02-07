package br.landucci.admin.catologo.application.category;

import br.landucci.admin.catologo.application.category.list.DefaultListCategoriesUseCase;
import br.landucci.admin.catologo.application.category.list.ListCategoriesOutputCommand;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

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
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 7;
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        final var expectedResult = expectedPagination.map(ListCategoriesOutputCommand::from);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        Mockito.when(gateway.findAll(Mockito.eq(query))).thenReturn(expectedPagination);
        final var result = useCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, result.items().size());
        Assertions.assertEquals(expectedResult, result);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(categories.size(), result.total());
    }

    @Test
    public void givenAValidInput_whenListingCategoriesWithNoResoults_thenShouldReturnAnEmptyListOfCategories() {
        final var categories = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        final var expectedResult = expectedPagination.map(ListCategoriesOutputCommand::from);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        Mockito.when(gateway.findAll(Mockito.eq(query))).thenReturn(expectedPagination);
        final var result = useCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, result.items().size());
        Assertions.assertEquals(expectedResult, result);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(categories.size(), result.total());
    }

    @Test
    public void givenAValidInput_whenGatewayThrowsAnException_thenShouldReturnAnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedErrorMessage = "Generic gateway error";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        Mockito.when(gateway.findAll(Mockito.eq(query))).thenThrow(new IllegalStateException(expectedErrorMessage));
        final var exception = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }
}

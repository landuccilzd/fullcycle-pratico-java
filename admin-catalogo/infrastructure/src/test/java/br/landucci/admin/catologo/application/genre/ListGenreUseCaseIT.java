package br.landucci.admin.catologo.application.genre;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.application.genre.list.ListGenreOutputCommand;
import br.landucci.admin.catologo.application.genre.list.ListGenreUseCase;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreJpaEntity;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
class ListGenreUseCaseIT {

    @Autowired
    private ListGenreUseCase useCase;

    @Autowired
    private GenreGateway gateway;

    @Autowired
    private GenreRepository repository;

    @Test
    void givenAValidQuery_whenListingGenres_shouldReturnGenres() {
        final var genres = List.of(
                Genre.newGenre("Ação", true),
                Genre.newGenre("Aventura", true)
        );

        this.repository.saveAllAndFlush(genres.stream().map(GenreJpaEntity::from).toList());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream().map(ListGenreOutputCommand::from).toList();
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = this.useCase.execute(query);

        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedItems.size(), output.items().size());
        Assertions.assertTrue(expectedItems.containsAll(output.items()));
    }

    @Test
    void givenAValidQuery_whenListingGenresAndResultIsEmpty_shouldReturnGenres() {
        final var genres = List.<Genre>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedItems = List.<ListGenreOutputCommand>of();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = useCase.execute(query);

        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedItems, output.items());
    }

}
package br.landucci.admin.catologo.application.video;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.genre.list.ListGenreOutputCommand;
import br.landucci.admin.catologo.application.video.list.DefaultListVideosUseCase;
import br.landucci.admin.catologo.application.video.list.ListVideoOutputCommand;
import br.landucci.admin.catologo.application.Fixture;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.video.VideoGateway;
import br.landucci.admin.catologo.domain.video.VideoPreview;
import br.landucci.admin.catologo.domain.video.VideoSearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListVideosUseCase useCase;

    @Mock
    private VideoGateway gateway;

    @Test
    void givenAValidQuery_whenListingVideos_thenShouldReturnALisOfVideos() {
        final var videos = List.of(
                new VideoPreview(Fixture.Videos.video()),
                new VideoPreview(Fixture.Videos.video())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;
        final var expectedItems = videos.stream().map(ListVideoOutputCommand::from).toList();
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, videos);

        Mockito.when(gateway.findAll(Mockito.any())).thenReturn(expectedPagination);

        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of()
        );

        final var output = useCase.execute(query);

        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedItems, output.items());

        Mockito.verify(gateway, Mockito.times(1)).findAll(query);
    }

    @Test
    void givenAValidQuery_whenListingVideosAndResultIsEmpty_thenShouldReturnGenres() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedItems = List.<ListGenreOutputCommand>of();
        final var videos = List.<VideoPreview>of();
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, videos);

        Mockito.when(gateway.findAll(Mockito.any())).thenReturn(expectedPagination);

        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of());

        final var output = useCase.execute(query);

        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedItems, output.items());

        Mockito.verify(gateway, Mockito.times(1)).findAll(query);
    }

    @Test
    void givenAValidQuery_whenListingVideosAndGatewayThrowsRandomError_thenShouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        Mockito.when(gateway.findAll(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of());

        final var actualOutput = Assertions.assertThrows(IllegalStateException.class, () ->
                useCase.execute(query)
        );

        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());
        Mockito.verify(gateway, Mockito.times(1)).findAll(query);
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

}
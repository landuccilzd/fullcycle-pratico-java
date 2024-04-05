package br.landucci.admin.catologo.application.castmember;

import br.landucci.admin.catologo.application.Fixture;
import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.castmember.list.DefaultListCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.list.ListCastMemberOutputCommand;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListCastMembersUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCastMemberUseCase useCase;
    @Mock
    private CastMemberGateway gateway;

    @Test
    public void givenAValidQuery_whenListingCastMembers_thenShouldReturnAll() {
        final var members = List.of(
                CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type()),
                CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream()
                .map(ListCastMemberOutputCommand::from)
                .toList();

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, members);

        Mockito.when(gateway.findAll(Mockito.any())).thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = useCase.execute(query);

        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedItems, output.items());
        Mockito.verify(gateway).findAll(ArgumentMatchers.eq(query));
    }

    @Test
    public void givenAValidQuery_whenListingCastMembersAndIsEmpty_thenShouldReturn() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var members = List.<CastMember>of();
        final var expectedItems = List.<ListCastMemberOutputCommand>of();
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, members);

        Mockito.when(gateway.findAll(Mockito.any())).thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = useCase.execute(query);

        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedItems, output.items());
        Mockito.verify(gateway).findAll(ArgumentMatchers.eq(query));
    }

    @Test
    public void givenAValidQuery_whenListingCastMembersAndGatewayThrowsRandomException_thenShouldReturnAnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        Mockito.when(gateway.findAll(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var exception = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        Mockito.verify(gateway).findAll(ArgumentMatchers.eq(query));
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

}

package br.landucci.admin.catologo.application.castmember;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.application.castmember.list.ListCastMemberOutputCommand;
import br.landucci.admin.catologo.application.castmember.list.ListCastMemberUseCase;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

@IntegrationTest
class ListCastMembersUseCaseIT {

    @Autowired
    private ListCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;

    @Test
    void givenAValidQuery_whenListingCastMembers_thenShouldReturnAll() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDir = "asc";
        final var expectedTotal = 2;

        final var castMembers = List.of(
                CastMember.newCastMember("Zelda", CastMemberType.ACTOR),
                CastMember.newCastMember("Peach", CastMemberType.DIRECTOR)
        );

        this.repository.saveAllAndFlush(
                castMembers.stream()
                        .map(CastMemberJpaEntity::from)
                        .toList()
        );

        Assertions.assertEquals(2, this.repository.count());

        final var expectedItems = castMembers.stream()
                .map(ListCastMemberOutputCommand::from)
                .toList();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDir);
        final var output = this.useCase.execute(query);

        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedItems.size(), output.items().size());
        Assertions.assertTrue(expectedItems.containsAll(output.items()));
        Mockito.verify(this.gateway).findAll(Mockito.any());
    }

    @Test
    void givenAnInexistingCastMembers_whenListing_thenShouldReturnNothing() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDir = "asc";
        final var expectedTotal = 0;
        final var expectedItems = List.<ListCastMemberOutputCommand>of();

        Assertions.assertEquals(0, this.repository.count());

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDir);
        final var output = useCase.execute(query);

        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedItems, output.items());

        Mockito.verify(gateway).findAll(Mockito.any());
    }

    @Test
    void givenAValidQuery_whenListingCastMembersAndGatewayThrowsAnException_thenShouldReturnAnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDir = "asc";
        final var expectedErrorMessage = "Gateway error";

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage)).when(this.gateway).findAll(Mockito.any());

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDir);
        final var actualException = Assertions.assertThrows(IllegalStateException.class,
                () -> this.useCase.execute(query));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(gateway).findAll(Mockito.any());
    }

}
package br.landucci.admin.catologo.infrastructure.castmember;

import br.landucci.admin.catologo.MySQLGatewayTest;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway gateway;

    @Autowired
    private CastMemberRepository repository;

    @Test
    public void givenSomeValidParams_whenCreatingACastMember_thenShouldPersistTheCastMember() {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;
        final var excpectedCastMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = excpectedCastMember.getId();

        Assertions.assertEquals(0, repository.count());

        final var castMember = this.gateway.create(excpectedCastMember.clone());

        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(expectedId, castMember.getId());
        Assertions.assertEquals(expectedName, castMember.getName());
        Assertions.assertEquals(expectedType, castMember.getType());
        Assertions.assertEquals(excpectedCastMember.getCreatedAt(), castMember.getCreatedAt());
        Assertions.assertEquals(excpectedCastMember.getUpdatedAt(), castMember.getUpdatedAt());

        final var persistedMember = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(castMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(castMember.getUpdatedAt(), persistedMember.getUpdatedAt());
    }

    @Test
    public void givenAValidCastMember_whenUpdating_shouldPersistTheUpdatedCastMember() {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedCastMember = CastMember.newCastMember("Peach", CastMemberType.DIRECTOR);
        final var expectedId = expectedCastMember.getId();

        final var castMemberEntity = repository.saveAndFlush(CastMemberJpaEntity.from(expectedCastMember));

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals("Peach", castMemberEntity.getName());
        Assertions.assertEquals(CastMemberType.DIRECTOR, castMemberEntity.getType());

        final var castMember = expectedCastMember.clone()
                .updateName(expectedName).updateType(expectedType);

        final var updatedCastMember = gateway.update(castMember);

        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(expectedId, updatedCastMember.getId());
        Assertions.assertEquals(expectedName, updatedCastMember.getName());
        Assertions.assertEquals(expectedType, updatedCastMember.getType());
        Assertions.assertEquals(expectedCastMember.getCreatedAt(), updatedCastMember.getCreatedAt());
        Assertions.assertTrue(expectedCastMember.getUpdatedAt().isBefore(updatedCastMember.getUpdatedAt()));

        final var retrievedCastMember = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), retrievedCastMember.getId());
        Assertions.assertEquals(expectedName, retrievedCastMember.getName());
        Assertions.assertEquals(expectedType, retrievedCastMember.getType());
        Assertions.assertEquals(expectedCastMember.getCreatedAt(), retrievedCastMember.getCreatedAt());
        Assertions.assertTrue(expectedCastMember.getUpdatedAt().isBefore(retrievedCastMember.getUpdatedAt()));
    }

    @Test
    public void givenTwoCastMembersAndOnePersisted_whenCallingExistsByIds_shouldReturnOnlyThePersistedID() {
        final var expectedCastMember = CastMember.newCastMember("Zelda", CastMemberType.DIRECTOR);
        final var expectedItems = 1;
        final var expectedId = expectedCastMember.getId();

        Assertions.assertEquals(0, repository.count());
        repository.saveAndFlush(CastMemberJpaEntity.from(expectedCastMember));
        Assertions.assertEquals(1, repository.count());

        final var actualMember = gateway.existsByIds(List.of(CastMemberID.with("123"), expectedId));

        Assertions.assertEquals(expectedItems, actualMember.size());
        Assertions.assertEquals(expectedId.getValue(), actualMember.get(0).getValue());
    }

    @Test
    public void givenAValidCastMember_whenDeletingById_thenShouldDeleteTheCastMember() {
        final var castMember = CastMember.newCastMember("Zelda", CastMemberType.ACTOR);

        repository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        Assertions.assertEquals(1, repository.count());

        gateway.deleteById(castMember.getId());

        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAnInvalidId_whenDeletingById_thenShouldBeIgnored() {
        final var castMember = CastMember.newCastMember("Zelda", CastMemberType.ACTOR);

        repository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        Assertions.assertEquals(1, repository.count());

        gateway.deleteById(CastMemberID.with("123"));

        Assertions.assertEquals(1, repository.count());
    }

    @Test
    public void givenAValidCastMember_whenFindindById_thenShouldReturnTheFoundCastMember() {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;
        final var castMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        repository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        Assertions.assertEquals(1, repository.count());

        final var foundCastMember = gateway.findById(expectedId).get();

        Assertions.assertEquals(expectedId, foundCastMember.getId());
        Assertions.assertEquals(expectedName, foundCastMember.getName());
        Assertions.assertEquals(expectedType, foundCastMember.getType());
        Assertions.assertEquals(castMember.getCreatedAt(), foundCastMember.getCreatedAt());
        Assertions.assertEquals(castMember.getUpdatedAt(), foundCastMember.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidId_whenFindingById_thenShouldReturnEmpty() {
        final var castMember = CastMember.newCastMember("Zelda", CastMemberType.ACTOR);

        repository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        Assertions.assertEquals(1, repository.count());

        final var foundMember = gateway.findById(CastMemberID.with("123"));

        Assertions.assertTrue(foundMember.isEmpty());
    }

    @Test
    public void givenNonExisteingCastMembers_whenFindingAll_thenShouldReturnEmpty() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 0;

        final var query = new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");
        final var actualPage = gateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "zel,0,10,1,1,Zelda",
            "pea,0,10,1,1,Peach",
            "dai,0,10,1,1,Daisy",
            "kat,0,10,1,1,Katara",
            "SHE,0,10,1,1,She Ra",
    })
    public void givenAValidTerm_whenFindingAll_thenShouldReturnFiltered(final String expectedTerms,
            final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal,
            final String expectedName) {
        mockMembers();
        final var expectedSort = "name";
        final var expectedDir = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDir);
        final var page = gateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedItemsCount, page.items().size());
        Assertions.assertEquals(expectedName, page.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Daisy",
            "name,desc,0,10,5,5,Zelda"
    })
    public void givenAValidSortAndDirection_whenFindingAll_thenShouldReturnSorted(final String expectedSort,
            final String expectedDir, final int expectedPage, final int expectedPerPage,
            final int expectedItemsCount, final long expectedTotal, final String expectedName) {
        mockMembers();
        final var expectedTerms = "";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDir);
        final var page = gateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedItemsCount, page.items().size());
        Assertions.assertEquals(expectedName, page.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Daisy;Katara",
            "1,2,2,5,Peach;She Ra",
            "2,2,1,5,Zelda",
    })
    public void givenSomeValidParams_whenListing_thenShouldReturnCastMembersPaginated(final int expectedPage,
            final int expectedPerPage, final int expectedItemsCount, final long expectedTotal,
            final String expectedNames) {
        mockMembers();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDir = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDir);
        final var page = gateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedItemsCount, page.items().size());

        int index = 0;
        for (final var expectedName : expectedNames.split(";")) {
            Assertions.assertEquals(expectedName, page.items().get(index++).getName());
        }
    }

    private void mockMembers() {
        repository.saveAllAndFlush(List.of(
                CastMemberJpaEntity.from(CastMember.newCastMember("Zelda", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("Peach", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("Daisy", CastMemberType.DIRECTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("Katara", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("She Ra", CastMemberType.DIRECTOR))
        ));
    }
}
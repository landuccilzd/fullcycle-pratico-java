package br.landucci.admin.catologo.application.castmember;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.application.castmember.find.FindCastMemberByIDInputCommand;
import br.landucci.admin.catologo.application.castmember.find.FindCastMemberByIDUseCase;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberJpaEntityBuilder;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class FindCastMemberByIDUseCaseIT {

    @Autowired
    private FindCastMemberByIDUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;

    @Test
    public void givenAValidId_whenFindingCastMemberByID_thenShouldReturnTheFoundCastMember() {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.DIRECTOR;
        final var castMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        this.repository.saveAndFlush(CastMemberJpaEntity.from(castMember));

        Assertions.assertEquals(1, this.repository.count());

        final var input = FindCastMemberByIDInputCommand.with(expectedId.getValue());
        final var output = this.useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedType, output.type());
        Assertions.assertEquals(castMember.getCreatedAt(), output.createdAt());
        Assertions.assertEquals(castMember.getUpdatedAt(), output.updatedAt());

        Mockito.verify(this.gateway).findById(Mockito.any());
    }

    @Test
    public void givenAnInvalidId_whenFindingCastMember_thenShouldReturnNotFoundException() {
        final var expectedId = CastMemberID.with("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var input = FindCastMemberByIDInputCommand.with(expectedId.getValue());
        final var output = Assertions.assertThrows(NotFoundException.class,
                () -> this.useCase.execute(input));

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedErrorMessage, output.getMessage());

        Mockito.verify(this.gateway).findById(ArgumentMatchers.eq(expectedId));
    }
}

package br.landucci.admin.catologo.application.castmember;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.application.castmember.update.UpdateCastMemberInputCommand;
import br.landucci.admin.catologo.application.castmember.update.UpdateCastMemberUseCase;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
class UpdateCastMemberUseCaseIT {

    @Autowired
    private UpdateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;

    @Test
    void givenAValidCommand_whenUpdatingACastMember_thenShouldReturnItsIdentifier() {
        final var castMember = CastMember.newCastMember("Zelda", CastMemberType.DIRECTOR);
        final var expectedId = castMember.getId();
        final var expectedName = "Peach";
        final var expectedType = CastMemberType.ACTOR;

        this.repository.saveAndFlush(CastMemberJpaEntity.from(castMember));

        final var input = UpdateCastMemberInputCommand.with(expectedId.getValue(), expectedName, expectedType);
        final var output = this.useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());

        final var foundCastMember = this.repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, foundCastMember.getName());
        Assertions.assertEquals(expectedType, foundCastMember.getType());
        Assertions.assertEquals(castMember.getCreatedAt(), foundCastMember.getCreatedAt());
        Assertions.assertTrue(castMember.getUpdatedAt().isBefore(foundCastMember.getUpdatedAt()));

        Mockito.verify(this.gateway).findById(Mockito.any());
        Mockito.verify(this.gateway).update(Mockito.any());
    }

    @Test
    void givenAnInvalidNullName_whenUpdatingACastMember_thenShouldThrowsNotificationException() {
        final var castMember = CastMember.newCastMember("Zelda", CastMemberType.DIRECTOR);
        final var expectedId = castMember.getId();
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        this.repository.saveAndFlush(CastMemberJpaEntity.from(castMember));

        final var input = UpdateCastMemberInputCommand.with(expectedId.getValue(), null, expectedType);
        final var exception = Assertions.assertThrows(NotificationException.class, () -> this.useCase.execute(input));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(gateway).findById(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidNullType_whenUpdatingACastMember_thenShouldThrowsNotificationException() {
        final var castMember = CastMember.newCastMember("Zelda", CastMemberType.DIRECTOR);
        final var expectedId = castMember.getId();
        final var expectedName = "Peach";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Type should not be null";

        this.repository.saveAndFlush(CastMemberJpaEntity.from(castMember));

        final var input = UpdateCastMemberInputCommand.with(expectedId.getValue(), expectedName, null);
        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> this.useCase.execute(input));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(this.gateway).findById(Mockito.any());
        Mockito.verify(this.gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidId_whenUpdatingACastMember_thenShouldThrowsNotFoundException() {
        final var expectedId = CastMemberID.with("123");
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var input = UpdateCastMemberInputCommand.with(expectedId.getValue(), expectedName, expectedType);
        final var exception = Assertions.assertThrows(NotFoundException.class,
                () -> this.useCase.execute(input));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());

        Mockito.verify(gateway).findById(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

}
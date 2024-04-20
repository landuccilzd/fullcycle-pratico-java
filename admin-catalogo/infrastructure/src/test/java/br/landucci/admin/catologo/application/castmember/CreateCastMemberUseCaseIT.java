package br.landucci.admin.catologo.application.castmember;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.application.castmember.create.CreateCastMemberInputCommand;
import br.landucci.admin.catologo.application.castmember.create.CreateCastMemberUseCase;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
class CreateCastMemberUseCaseIT {

    @Autowired
    private CreateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;

    @Test
    void givenAValidCommand_whenCreatingCastMember_thenShouldReturnTheCreatedCastMember() {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.DIRECTOR;

        final var input = CreateCastMemberInputCommand.with(expectedName, expectedType);
        final var output = this.useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var castMemberEntity = this.repository.findById(output.id()).get();

        Assertions.assertEquals(expectedName, castMemberEntity.getName());
        Assertions.assertEquals(expectedType, castMemberEntity.getType());
        Assertions.assertNotNull(castMemberEntity.getCreatedAt());
        Assertions.assertNotNull(castMemberEntity.getUpdatedAt());
        Assertions.assertEquals(castMemberEntity.getCreatedAt(), castMemberEntity.getUpdatedAt());

        Mockito.verify(this.gateway).create(Mockito.any());
    }

    @Test
    void givenAnInvalidNullName_whenCreatingACastMember_thenShouldThrowsANotificationException() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        final var input = CreateCastMemberInputCommand.with(null, CastMemberType.DIRECTOR);
        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> this.useCase.execute(input));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(this.gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidEmptyName_whenCreatingACastMember_thenShouldThrowsANotificationException() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be empty";

        final var input = CreateCastMemberInputCommand.with("", CastMemberType.DIRECTOR);
        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> this.useCase.execute(input));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(this.gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAnInvalidNullType_whenCreatingACastMember_thenShouldThrowsANotificationException() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Type should not be null";

        final var input = CreateCastMemberInputCommand.with("Zelda", null);
        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> this.useCase.execute(input));

        // then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(this.gateway, Mockito.times(0)).create(Mockito.any());
    }
}
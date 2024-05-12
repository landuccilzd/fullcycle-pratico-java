package br.landucci.admin.catologo.application.castmember;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.update.UpdateCastMemberInputCommand;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway gateway;

    @Test
    void givenAValidCommand_whenUpdatingACastMember_thenShouldReturnItsIdentifier() {
        final var castMember = CastMember.newCastMember("Peach", CastMemberType.ACTOR);
        final var expectedId = castMember.getId();
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.DIRECTOR;
        final var command = UpdateCastMemberInputCommand.with(expectedId.getValue(), expectedName, expectedType);

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(castMember));
        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());

        Mockito.verify(gateway).findById(expectedId);

        Mockito.verify(gateway).update(Mockito.argThat(updatedCastMember ->
                Objects.equals(expectedId, updatedCastMember.getId()) &&
                Objects.equals(expectedName, updatedCastMember.getName()) &&
                Objects.equals(expectedType, updatedCastMember.getType()) &&
                Objects.equals(castMember.getCreatedAt(), updatedCastMember.getCreatedAt())
        ));
    }

    @Test
    void givenAnInvalidName_whenUpdatingACastMember_thenShouldThrowNotificationException() {
        final var castMember = CastMember.newCastMember("Zelda", CastMemberType.DIRECTOR);
        final var expectedId = castMember.getId();
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        final var aCommand = UpdateCastMemberInputCommand.with(expectedId.getValue(), null, expectedType);

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(castMember));

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
        Mockito.verify(gateway).findById(expectedId);
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidType_whenUpdatingCastMember_thenShouldThrowANotificationException() {
        final var castMember = CastMember.newCastMember("Peach", CastMemberType.ACTOR);
        final var expectedId = castMember.getId();
        final var expectedName = "Zelda";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Type should not be null";

        final var command = UpdateCastMemberInputCommand.with(expectedId.getValue(), expectedName, null);

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(castMember));

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
        Mockito.verify(gateway).findById(expectedId);
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAnInvalidId_whenUpdatingACastMember_thenShouldThrowANotFoundException() {
        final var expectedId = CastMemberID.with("123");
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var command = UpdateCastMemberInputCommand.with(expectedId.getValue(), expectedName, expectedType);

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        Mockito.verify(gateway).findById(expectedId);
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }
}

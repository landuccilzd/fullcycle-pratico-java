package br.landucci.admin.catologo.application.castmember;

import br.landucci.admin.catologo.application.Fixture;
import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.castmember.create.CreateCastMemberInputCommand;
import br.landucci.admin.catologo.application.castmember.create.DefaultCreateCastMemberUseCase;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

public class CreateCastMemberUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;
    @Mock
    private CastMemberGateway gateway;

    @Test
    public void givenAValidCommand_whenCreatingACastMember_thenShouldReturnTheCreatedCastMember() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = CreateCastMemberInputCommand.with(expectedName, expectedType);
        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway).create(Mockito.argThat(castMember ->
                Objects.nonNull(castMember.getId()) &&
                Objects.equals(expectedName, castMember.getName()) &&
                Objects.equals(expectedType, castMember.getType()) &&
                Objects.nonNull(castMember.getCreatedAt()) &&
                Objects.nonNull(castMember.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidNullName_whenCreatingACastMember_thenShouldThrowsNotificationException() {
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        final var command = CreateCastMemberInputCommand.with(null, expectedType);

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidNullType_whenCreatingACastMember_thenShouldThrowsNotificationException() {
        final var expectedName = Fixture.name();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Type should not be null";

        final var command = CreateCastMemberInputCommand.with(expectedName, null);
        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }


    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

}
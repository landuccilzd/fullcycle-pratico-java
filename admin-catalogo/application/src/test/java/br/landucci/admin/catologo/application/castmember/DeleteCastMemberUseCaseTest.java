package br.landucci.admin.catologo.application.castmember;

import br.landucci.admin.catologo.application.Fixture;
import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.delete.DeleteCastMemberInputCommand;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;
    @Mock
    private CastMemberGateway gateway;

    @Test
    public void givenAValidId_whenDeletingACastMember_shouldDeleteTyheCastMember() {
        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = castMember.getId();

        Mockito.doNothing().when(gateway).deleteById(Mockito.any());

        final var command = DeleteCastMemberInputCommand.from(expectedId);
        Assertions.assertDoesNotThrow(() -> useCase.execute(command));
        Mockito.verify(gateway).deleteById(ArgumentMatchers.eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenDeletingACastMember_shouldBeOk() {
        final var expectedId = CastMemberID.with("123");

        Mockito.doNothing().when(gateway).deleteById(Mockito.any());

        final var command = DeleteCastMemberInputCommand.from(expectedId);
        Assertions.assertDoesNotThrow(() -> useCase.execute(command));
        Mockito.verify(gateway).deleteById(ArgumentMatchers.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        final var castMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = castMember.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error")).when(gateway).deleteById(Mockito.any());

        final var command = DeleteCastMemberInputCommand.from(expectedId);
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(command));
        Mockito.verify(gateway).deleteById(ArgumentMatchers.eq(expectedId));
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }
}

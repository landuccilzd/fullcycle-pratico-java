package br.landucci.admin.catologo.application.castmember;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.castmember.find.DefaultFindCastMemberByIDUseCase;
import br.landucci.admin.catologo.application.castmember.find.FindCastMemberByIDInputCommand;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class FindCastMemberByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultFindCastMemberByIDUseCase useCase;
    @Mock
    private CastMemberGateway gateway;

    @Test
    void givenAValidId_whenFindingACastMember_shouldReturnTheCastMember() {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;
        final var castMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(castMember));

        final var command = FindCastMemberByIDInputCommand.from(expectedId);
        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedType, output.type());
        Assertions.assertEquals(castMember.getCreatedAt(), output.createdAt());
        Assertions.assertEquals(castMember.getUpdatedAt(), output.updatedAt());

        Mockito.verify(gateway).findById(expectedId);
    }

    @Test
    void givenAInvalidId_whenFindingCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        final var expectedId = CastMemberID.with("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.empty());

        final var command = FindCastMemberByIDInputCommand.from(expectedId);
        final var output = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(command));

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedErrorMessage, output.getMessage());
        Mockito.verify(gateway).findById(expectedId);
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

}

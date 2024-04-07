package br.landucci.admin.catologo.application.castmember;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.application.castmember.delete.DeleteCastMemberInputCommand;
import br.landucci.admin.catologo.application.castmember.delete.DeleteCastMemberUseCase;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {
    @Autowired
    private DeleteCastMemberUseCase useCase;
    @Autowired
    private CastMemberRepository repository;
    @SpyBean
    private CastMemberGateway gateway;

    @Test
    public void givenAValidId_whenDeletingCastMember_thenShouldDeleteIt() {
        final var zelda = CastMember.newCastMember("Zelda", CastMemberType.DIRECTOR);
        final var peach = CastMember.newCastMember("Peach", CastMemberType.ACTOR);
        final var expectedId = zelda.getId();

        this.repository.saveAndFlush(CastMemberJpaEntity.from(zelda));
        this.repository.saveAndFlush(CastMemberJpaEntity.from(peach));

        Assertions.assertEquals(2, this.repository.count());

        final var input = DeleteCastMemberInputCommand.with(expectedId.getValue());
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(input));

        Assertions.assertEquals(1, this.repository.count());
        Assertions.assertFalse(this.repository.existsById(expectedId.getValue()));
        Assertions.assertTrue(this.repository.existsById(peach.getId().getValue()));

        Mockito.verify(this.gateway).deleteById(ArgumentMatchers.eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenDeletingACastMember_thenShouldDoNothing() {
        final var zelda = CastMember.newCastMember("Zelda", CastMemberType.DIRECTOR);
        final var expectedId = CastMemberID.with("123");

        this.repository.saveAndFlush(CastMemberJpaEntity.from(zelda));

        Assertions.assertEquals(1, this.repository.count());

        final var input = DeleteCastMemberInputCommand.with(expectedId.getValue());
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(input));

        Mockito.verify(this.gateway).deleteById(ArgumentMatchers.eq(expectedId));

        Assertions.assertEquals(1, this.repository.count());
    }

    @Test
    public void givenAValidId_whenDeletingCastMemberAndGatewayThrowsException_thenShouldReceiveAnException() {
        // given
        final var zelda = CastMember.newCastMember("Zelda", CastMemberType.ACTOR);
        final var expectedId = zelda.getId();

        this.repository.saveAndFlush(CastMemberJpaEntity.from(zelda));

        Assertions.assertEquals(1, this.repository.count());

        Mockito.doThrow(new IllegalStateException("Gateway error")).when(this.gateway).deleteById(Mockito.any());

        final var input = DeleteCastMemberInputCommand.with(expectedId.getValue());
        Assertions.assertThrows(IllegalStateException.class, () -> this.useCase.execute(input));

        Mockito.verify(this.gateway).deleteById(ArgumentMatchers.eq(expectedId));

        Assertions.assertEquals(1, this.repository.count());
    }
}

package br.landucci.admin.catologo.application.castmember.delete;

import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;

import java.util.Objects;

public final class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {

    private final CastMemberGateway gateway;

    public DefaultDeleteCastMemberUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final DeleteCastMemberInputCommand command) {
        this.gateway.deleteById(CastMemberID.with(command.id()));
    }
}

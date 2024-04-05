package br.landucci.admin.catologo.application.castmember.find;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.exception.NotFoundException;

import java.util.Objects;

public final class DefaultFindCastMemberByIDUseCase extends FindCastMemberByIDUseCase {

    private final CastMemberGateway gateway;

    public DefaultFindCastMemberByIDUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public FindCastMemberByIDOutputCommand execute(final FindCastMemberByIDInputCommand command) {
        final var id = CastMemberID.with(command.id());
        return this.gateway.findById(id)
                .map(FindCastMemberByIDOutputCommand::from)
                .orElseThrow(() -> NotFoundException.with(CastMember.class, id));
    }

}
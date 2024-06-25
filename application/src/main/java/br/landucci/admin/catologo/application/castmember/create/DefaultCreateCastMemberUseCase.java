package br.landucci.admin.catologo.application.castmember.create;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.validation.handler.Notification;

import java.util.Objects;

public final class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {

    private final CastMemberGateway gateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CreateCastMemberOutputCommand execute(final CreateCastMemberInputCommand input) {
        final var name = input.name();
        final var type = input.type();

        final var notification = Notification.create();
        final var castMember = notification.validate(() -> CastMember.newCastMember(name, type));

        if (notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Cast Member", notification);
        }

        final var output = this.gateway.create(castMember);
        return CreateCastMemberOutputCommand.from(output);
    }
}

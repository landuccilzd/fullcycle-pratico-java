package br.landucci.admin.catologo.application.castmember.update;

import br.landucci.admin.catologo.domain.Identifier;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public final class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {
    private final CastMemberGateway gateway;

    public DefaultUpdateCastMemberUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public UpdateCastMemberOutputCommand execute(final UpdateCastMemberInputCommand input) {
        final var id = CastMemberID.with(input.id());
        final var name = input.name();
        final var type = input.type();

        final var castMember = this.gateway.findById(id).orElseThrow(notFound(id));

        final var notification = Notification.create();
        notification.validate(() -> castMember.updateName(name).updateType(type));

        if (notification.hasErrors()) {
            throw new NotificationException("Could not update Aggregate Cast Member %s".formatted(input.id()), notification);
        }

        final var output = this.gateway.update(castMember);
        return UpdateCastMemberOutputCommand.from(output);
    }

    private static Supplier<NotFoundException> notFound(final Identifier id) {
        return () -> NotFoundException.with(CastMember.class, id);
    }
}

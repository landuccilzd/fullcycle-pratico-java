package br.landucci.admin.catologo.domain.castmember;

import br.landucci.admin.catologo.domain.AggregateRoot;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.utils.InstantUtils;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;
import br.landucci.admin.catologo.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.Objects;

public class CastMember extends AggregateRoot<CastMemberID>  {
    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    private CastMember(CastMemberID id, String name, CastMemberType type, Instant createdAt, Instant updatedAt) {
        super(id);
        this.name = name;
        this.type = type;
        this.createdAt = Objects.requireNonNull(createdAt, "Created At should not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated At should not be null");
        this.selfValidate();
    }

    public static CastMember with(final CastMemberID id, final String name, final CastMemberType type,
            final Instant createdAt, final Instant updatedAt) {
        return new CastMember(id, name, type, createdAt, updatedAt);
    }

    public static CastMember newCastMember(final String name, final CastMemberType type) {
        final var id = CastMemberID.unique();
        final var now  = InstantUtils.now();
        return with(id, name, type, now, now);
    }

    public static CastMember clone(final CastMember castMember) {
        return with(castMember.getId(), castMember.getName(), castMember.getType(), castMember.getCreatedAt(),
                castMember.getUpdatedAt());
    }

    public String getName() {
        return this.name;
    }
    public CastMemberType getType() {
        return this.type;
    }
    public Instant getCreatedAt() {
        return this.createdAt;
    }
    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public CastMember updateName(final String name) {
        this.name = name;
        this.updatedAt = Instant.now();
        this.selfValidate();
        return this;
    }

    public CastMember updateType(final CastMemberType type) {
        this.type = type;
        this.updatedAt = Instant.now();
        this.selfValidate();
        return this;
    }
    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw new NotificationException("An validation error has ocurred: ", notification);
        }
    }

    @Override
    public void validate(final ValidationHandler handler) {
        final var validator = new CastMemberValidator(this, handler);
        validator.validate();
    }

}
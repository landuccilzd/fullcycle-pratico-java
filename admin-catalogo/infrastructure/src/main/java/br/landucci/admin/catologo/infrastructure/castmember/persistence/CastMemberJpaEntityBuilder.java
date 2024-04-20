package br.landucci.admin.catologo.infrastructure.castmember.persistence;

import br.landucci.admin.catologo.domain.castmember.CastMemberType;

import java.time.Instant;

public final class CastMemberJpaEntityBuilder {
    protected String id;
    protected String name;
    protected CastMemberType type;
    protected Instant createdAt;
    protected Instant updatedAt;

    public CastMemberJpaEntityBuilder withId(String id) {
        this.id = id;
        return this;
    }
    public CastMemberJpaEntityBuilder withName(String name) {
        this.name = name;
        return this;
    }
    public CastMemberJpaEntityBuilder withType(CastMemberType type) {
        this.type = type;
        return this;
    }
    public CastMemberJpaEntityBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public CastMemberJpaEntityBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    public CastMemberJpaEntity build() {
        return new CastMemberJpaEntity(this);
    }
}
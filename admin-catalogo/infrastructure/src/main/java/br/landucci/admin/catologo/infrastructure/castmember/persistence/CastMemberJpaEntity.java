package br.landucci.admin.catologo.infrastructure.castmember.persistence;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "cast_members")
public class CastMemberJpaEntity {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CastMemberType type;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public CastMemberJpaEntity() {}

    protected CastMemberJpaEntity(final CastMemberJpaEntityBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.type = builder.type;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static CastMemberJpaEntity from(final CastMember castMember) {
        return new CastMemberJpaEntityBuilder().withId(castMember.getId().getValue()).withName(castMember.getName())
                .withType(castMember.getType()).withCreatedAt(castMember.getCreatedAt())
                .withUpdatedAt(castMember.getUpdatedAt()).build();
    }

    public CastMember toAggregate() {
        return CastMember.with(CastMemberID.with(getId()), getName(), getType(), getCreatedAt(), getUpdatedAt());
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public CastMemberType getType() {
        return type;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public CastMemberJpaEntity setId(final String id) {
        this.id = id;
        return this;
    }
    public CastMemberJpaEntity setName(final String name) {
        this.name = name;
        return this;
    }

    public CastMemberJpaEntity setType(final CastMemberType type) {
        this.type = type;
        return this;
    }

    public CastMemberJpaEntity setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public CastMemberJpaEntity setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastMemberJpaEntity that = (CastMemberJpaEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return getName();
    }
}

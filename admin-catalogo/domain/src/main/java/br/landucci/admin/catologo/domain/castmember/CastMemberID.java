package br.landucci.admin.catologo.domain.castmember;

import br.landucci.admin.catologo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CastMemberID extends Identifier {

    private final String value;

    private CastMemberID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static CastMemberID with(final String id) {
        return new CastMemberID(id);
    }

    public static CastMemberID from(final UUID uuid) {
        return with(uuid.toString());
    }

    public static CastMemberID unique() {
        final var uuid = UUID.randomUUID();
        return from(uuid);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastMemberID that = (CastMemberID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
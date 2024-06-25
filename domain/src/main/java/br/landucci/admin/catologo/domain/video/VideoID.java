package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {
    private final String value;

    private VideoID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static VideoID with(final String id) {
        return new VideoID(id);
    }

    public static VideoID from(final UUID uuid) {
        return with(uuid.toString().toLowerCase());
    }

    public static VideoID unique() {
        final var uuid = UUID.randomUUID();
        return from(uuid);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoID that = (VideoID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

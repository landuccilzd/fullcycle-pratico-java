package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.ValueObject;

import java.util.Objects;

public class ImageMedia extends ValueObject {

    private final String id;
    private final String checksum;
    private final String name;
    private final String location;

    private ImageMedia(final String id, final String checksum, final String name, final String location) {
        this.id = Objects.requireNonNull(id);
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.location = Objects.requireNonNull(location);
    }

    public static ImageMedia with(final String id, final String checksum, final String name, final String location) {
        return new ImageMedia(id, checksum, name, location);
    }

    public static ImageMedia with(final String checksum, final String name, final String location) {
        return new ImageMedia(VideoID.unique().getValue(), checksum, name, location);
    }

    public String getId() {
        return id;
    }
    public String getChecksum() {
        return checksum;
    }
    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ImageMedia that = (ImageMedia) o;
        return Objects.equals(checksum, that.checksum) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, location);
    }
}

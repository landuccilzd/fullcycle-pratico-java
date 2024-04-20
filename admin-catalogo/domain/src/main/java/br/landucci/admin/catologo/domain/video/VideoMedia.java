package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.ValueObject;

import java.util.Objects;

public class VideoMedia extends ValueObject {

    private final String id;
    private final String checksum;
    private final String name;
    private final String rawLocation;
    private final String encodedLocation;
    private final MediaStatus status;

    private VideoMedia(final String id, final String checksum, final String name, final String rawLocation,
            final String encodedLocation, final MediaStatus status) {
        this.id = Objects.requireNonNull(id);
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.encodedLocation = Objects.requireNonNull(encodedLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static VideoMedia with(final String id, final String checksum, final String name, final String rawLocation,
            final String encodedLocation, final MediaStatus status) {
        return new VideoMedia(id, checksum, name, rawLocation, encodedLocation, status);
    }

    public static VideoMedia with(final String checksum, final String name, final String rawLocation) {
        return with(VideoID.unique().getValue(), checksum, name, rawLocation, "", MediaStatus.PENDING);
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
    public String getRawLocation() {
        return rawLocation;
    }
    public String getEncodedLocation() {
        return encodedLocation;
    }
    public MediaStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoMedia that = (VideoMedia) o;
        return Objects.equals(checksum, that.checksum) && Objects.equals(rawLocation, that.rawLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, rawLocation);
    }

    public VideoMedia process() {
        return with(getId(), getChecksum(), getName(), getRawLocation(), getEncodedLocation(), MediaStatus.PROCESSING);
    }

    public VideoMedia complete(final String encodedPath) {
        return with(getId(), getChecksum(), getName(), getRawLocation(), encodedPath, MediaStatus.COMPLETED);
    }

    public boolean isPendingEncode() {
        return MediaStatus.PENDING == this.status;
    }
}

package br.landucci.admin.catologo.infrastructure.video.persistence;

import br.landucci.admin.catologo.domain.video.MediaStatus;
import br.landucci.admin.catologo.domain.video.VideoMedia;
import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "VideoMedia")
@Table(name = "video_video_media")
public class VideoMediaJpaEntity {

    @Id
    private String id;

    @Column(name = "checksum", nullable = false)
    private String checksum;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "encoded_path", nullable = false)
    private String encodedPath;

    @Column(name = "media_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaStatus status;

    public VideoMediaJpaEntity() {}

    private VideoMediaJpaEntity(final String id, final String checksum, final String name, final String filePath,
            final String encodedPath, final MediaStatus status) {

        this.id = id;
        this.checksum = checksum;
        this.name = name;
        this.filePath = filePath;
        this.encodedPath = encodedPath;
        this.status = status;
    }

    public static VideoMediaJpaEntity from(final VideoMedia media) {
        return new VideoMediaJpaEntity(media.getId(), media.getChecksum(), media.getName(), media.getRawLocation(),
                media.getEncodedLocation(), media.getStatus());
    }

    public VideoMedia toAggregate() {
        return VideoMedia.with(getId(), getChecksum(), getName(), getFilePath(), getEncodedPath(), getStatus());
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
    public String getFilePath() {
        return filePath;
    }
    public String getEncodedPath() {
        return encodedPath;
    }
    public MediaStatus getStatus() {
        return status;
    }

    public VideoMediaJpaEntity setId(String id) {
        this.id = id;
        return this;
    }
    public VideoMediaJpaEntity setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }
    public VideoMediaJpaEntity setName(String name) {
        this.name = name;
        return this;
    }
    public VideoMediaJpaEntity setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
    public VideoMediaJpaEntity setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
        return this;
    }
    public VideoMediaJpaEntity setStatus(MediaStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoMediaJpaEntity that = (VideoMediaJpaEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return name;
    }

}
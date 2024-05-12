package br.landucci.admin.catologo.infrastructure.video.persistence;

import br.landucci.admin.catologo.domain.video.ImageMedia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity(name = "ImageMedia")
@Table(name = "video_image_media")
public class ImageMediaJpaEntity {

    @Id
    private String id;

    @Column(name = "checksum", nullable = false)
    private String checksum;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    public ImageMediaJpaEntity() {}

    private ImageMediaJpaEntity(final String id, final String checksum, final String name, final String filePath) {
        this.id = id;
        this.checksum = checksum;
        this.name = name;
        this.filePath = filePath;
    }

    public static ImageMediaJpaEntity from(final ImageMedia media) {
        return new ImageMediaJpaEntity(media.getId(), media.getChecksum(), media.getName(), media.getLocation());
    }

    public ImageMedia toAggregate() {
        return ImageMedia.with(getId(), getChecksum(), getName(), getFilePath());
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

    public ImageMediaJpaEntity setId(String id) {
        this.id = id;
        return this;
    }
    public ImageMediaJpaEntity setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }
    public ImageMediaJpaEntity setName(String name) {
        this.name = name;
        return this;
    }
    public ImageMediaJpaEntity setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageMediaJpaEntity that = (ImageMediaJpaEntity) o;
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
package br.landucci.admin.catologo.infrastructure.video.persistence;

import br.landucci.admin.catologo.domain.category.CategoryID;
import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "VideoCategory")
@Table(name = "video_category")
public class VideoCategoryJpaEntity {

    @EmbeddedId
    private VideoCategoryID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoCategoryJpaEntity() {}

    private VideoCategoryJpaEntity(final VideoCategoryID id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCategoryJpaEntity from(final VideoJpaEntity video, final CategoryID category) {
        return new VideoCategoryJpaEntity(VideoCategoryID.from(video.getId(), category.getValue()), video);
    }

    public VideoCategoryID getId() {
        return id;
    }
    public VideoJpaEntity getVideo() {
        return video;
    }

    public VideoCategoryJpaEntity setId(VideoCategoryID id) {
        this.id = id;
        return this;
    }
    public VideoCategoryJpaEntity setVideo(VideoJpaEntity video) {
        this.video = video;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCategoryJpaEntity that = (VideoCategoryJpaEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVideo(), that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }

}

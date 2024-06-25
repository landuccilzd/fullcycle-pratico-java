package br.landucci.admin.catologo.infrastructure.video.persistence;

import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "VideoCastMember")
@Table(name = "video_cast_member")
public class VideoCastMemberJpaEntity {

    @EmbeddedId
    private VideoCastMemberID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoCastMemberJpaEntity() {}

    private VideoCastMemberJpaEntity(final VideoCastMemberID id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCastMemberJpaEntity from(final VideoJpaEntity entity, final CastMemberID castMemberID) {
        return new VideoCastMemberJpaEntity(VideoCastMemberID.from(entity.getId(), castMemberID.getValue()), entity);
    }

    public VideoCastMemberID getId() {
        return id;
    }
    public VideoJpaEntity getVideo() {
        return video;
    }

    public VideoCastMemberJpaEntity setId(VideoCastMemberID id) {
        this.id = id;
        return this;
    }
    public VideoCastMemberJpaEntity setVideo(VideoJpaEntity video) {
        this.video = video;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCastMemberJpaEntity that = (VideoCastMemberJpaEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVideo(), that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }

}

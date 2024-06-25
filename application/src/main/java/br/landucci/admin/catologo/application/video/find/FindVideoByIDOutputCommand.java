package br.landucci.admin.catologo.application.video.find;

import br.landucci.admin.catologo.domain.Identifier;
import br.landucci.admin.catologo.domain.utils.CollectionUtils;
import br.landucci.admin.catologo.domain.video.ImageMedia;
import br.landucci.admin.catologo.domain.video.Rating;
import br.landucci.admin.catologo.domain.video.Video;
import br.landucci.admin.catologo.domain.video.VideoMedia;

import java.time.Instant;
import java.util.Set;

public record FindVideoByIDOutputCommand(
        String id,
        Instant createdAt,
        Instant updatedAt,
        String title,
        String description,
        int launchedAt,
        double duration,
        boolean opened,
        boolean published,
        Rating rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers,
        ImageMedia banner,
        ImageMedia thumbnail,
        ImageMedia thumbnailHalf,
        VideoMedia video,
        VideoMedia trailer
) {

    public static FindVideoByIDOutputCommand from(final Video video) {
        return new FindVideoByIDOutputCommand(
                video.getId().getValue(),
                video.getCreatedAt(),
                video.getUpdatedAt(),
                video.getTitle(),
                video.getDescription(),
                video.getLaunchedAt().getValue(),
                video.getDuration(),
                video.isOpened(),
                video.isPublished(),
                video.getRating(),
                CollectionUtils.mapTo(video.getCategories(), Identifier::getValue),
                CollectionUtils.mapTo(video.getGenres(), Identifier::getValue),
                CollectionUtils.mapTo(video.getCastMembers(), Identifier::getValue),
                video.getBanner().orElse(null),
                video.getThumbnail().orElse(null),
                video.getThumbnailHalf().orElse(null),
                video.getContent().orElse(null),
                video.getTrailer().orElse(null)
        );
    }
}

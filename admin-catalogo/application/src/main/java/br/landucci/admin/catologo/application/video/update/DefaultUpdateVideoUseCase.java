package br.landucci.admin.catologo.application.video.update;

import br.landucci.admin.catologo.domain.Identifier;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.exception.InternalErrorException;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;
import br.landucci.admin.catologo.domain.validation.handler.Notification;
import br.landucci.admin.catologo.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase {

    private final VideoGateway gateway;
    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultUpdateVideoUseCase(
            final VideoGateway videoGateway,
            final CategoryGateway categoryGateway,
            final CastMemberGateway castMemberGateway,
            final GenreGateway genreGateway,
            final MediaResourceGateway mediaResourceGateway
    ) {
        this.gateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public UpdateVideoOutputCommand execute(final UpdateVideoInputCommand command) {
        final var id = VideoID.with(command.id());
        final var rating = Rating.of(command.rating()).orElse(null);
        final var launchYear = command.launchedAt() != null ? Year.of(command.launchedAt()) : null;
        final var categories = toIdentifier(command.categories(), CategoryID::from);
        final var genres = toIdentifier(command.genres(), GenreID::from);
        final var members = toIdentifier(command.members(), CastMemberID::with);

        final var video = this.gateway.findById(id).orElseThrow(notFoundException(id));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));

        video.updateTitle(command.title())
             .updateDescription(command.description())
             .updateLaunchedAt(launchYear)
             .updateDuration(command.duration())
             .updateRating(rating)
             .updateCategories(categories)
             .updateGenres(genres)
             .updateCastMembers(members);

        if (command.opened()) {
            video.open();
        } else {
            video.close();
        }

        if (command.published()) {
            video.publish();
        } else {
            video.unpublish();
        }

        video.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Could not update Aggregate Video: ", notification);
        }

        return UpdateVideoOutputCommand.from(update(command, video));
    }

    private Video update(final UpdateVideoInputCommand command, final Video video) {
        final var id = video.getId();

        try {
            final var videoMedia = command.getVideo().map(it ->
                    this.mediaResourceGateway.storeAudioVideo(id, VideoResource.with(VideoMediaType.VIDEO, it)))
                    .orElse(null);

            final var trailerMedia = command.getTrailer().map(it ->
                    this.mediaResourceGateway.storeAudioVideo(id, VideoResource.with(VideoMediaType.TRAILER, it)))
                    .orElse(null);

            final var bannerMedia = command.getBanner().map(it ->
                    this.mediaResourceGateway.storeImage(id, VideoResource.with(VideoMediaType.BANNER, it)))
                    .orElse(null);

            final var thumbnailMedia = command.getThumbnail().map(it ->
                    this.mediaResourceGateway.storeImage(id, VideoResource.with(VideoMediaType.THUMBNAIL, it)))
                    .orElse(null);

            final var thumbHalfMedia = command.getThumbnailHalf().map(it ->
                    this.mediaResourceGateway.storeImage(id, VideoResource.with(VideoMediaType.THUMBNAIL_HALF, it)))
                    .orElse(null);

            final var updatedMediaVideo = video
                    .updateContent(videoMedia)
                    .updateTrailer(trailerMedia)
                    .updateBanner(bannerMedia)
                    .updateThumbnail(thumbnailMedia)
                    .updateThumbnailHalf(thumbHalfMedia);

            final var output = this.gateway.update(updatedMediaVideo);
            return output;
        } catch (final Throwable t) {
            throw InternalErrorException.with(
                    "An error on create video was observed [videoId:%s]".formatted(id.getValue()), t);
        }
    }

    private Supplier<DomainException> notFoundException(final VideoID id) {
        return () -> NotFoundException.with(Video.class, id);
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return validateAggregate("categories", ids, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> ids) {
        return validateAggregate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateMembers(final Set<CastMemberID> ids) {
        return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(final String aggregate, final Set<T> ids,
            final Function<Iterable<T>, List<T>> existsByIds) {

        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new ValidationError(
                    "Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }

        return notification;
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream().map(mapper).collect(Collectors.toSet());
    }
}

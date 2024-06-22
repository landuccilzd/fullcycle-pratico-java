package br.landucci.admin.catologo.infrastructure.api.controllers;

import br.landucci.admin.catologo.application.video.create.CreateVideoInputCommand;
import br.landucci.admin.catologo.application.video.create.CreateVideoUseCase;
import br.landucci.admin.catologo.application.video.delete.DeleteVideoUseCase;
import br.landucci.admin.catologo.application.video.find.FindVideoByIDInputCommand;
import br.landucci.admin.catologo.application.video.find.FindVideoByIDUseCase;
import br.landucci.admin.catologo.application.video.list.ListVideosUseCase;
import br.landucci.admin.catologo.application.video.media.find.FindMediaByIDInputCommand;
import br.landucci.admin.catologo.application.video.media.find.FindMediaByIDUseCase;
import br.landucci.admin.catologo.application.video.media.upload.UploadMediaInputCommand;
import br.landucci.admin.catologo.application.video.media.upload.UploadMediaUseCase;
import br.landucci.admin.catologo.application.video.update.UpdateVideoInputCommand;
import br.landucci.admin.catologo.application.video.update.UpdateVideoUseCase;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.utils.CollectionUtils;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.video.Resource;
import br.landucci.admin.catologo.domain.video.VideoMediaType;
import br.landucci.admin.catologo.domain.video.VideoResource;
import br.landucci.admin.catologo.domain.video.VideoSearchQuery;
import br.landucci.admin.catologo.infrastructure.api.VideoAPI;
import br.landucci.admin.catologo.infrastructure.utils.HashingUtils;
import br.landucci.admin.catologo.infrastructure.video.models.CreateVideoRequestCommand;
import br.landucci.admin.catologo.infrastructure.video.models.UpdateVideoRequestCommand;
import br.landucci.admin.catologo.infrastructure.video.models.VideoListResponseCommand;
import br.landucci.admin.catologo.infrastructure.video.models.VideoResponseCommand;
import br.landucci.admin.catologo.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;
    private final FindVideoByIDUseCase findVideoByIdUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final ListVideosUseCase listVideosUseCase;
    private final FindMediaByIDUseCase findMediaByIDUseCase;
    private final UploadMediaUseCase uploadMediaUseCase;

    public VideoController(
            final CreateVideoUseCase createVideoUseCase,
            final FindVideoByIDUseCase findVideoByIdUseCase,
            final UpdateVideoUseCase updateVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase,
            final ListVideosUseCase listVideosUseCase,
            final FindMediaByIDUseCase findMediaByIDUseCase,
            final UploadMediaUseCase uploadMediaUseCase
    ) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.findVideoByIdUseCase = Objects.requireNonNull(findVideoByIdUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.listVideosUseCase = Objects.requireNonNull(listVideosUseCase);
        this.findMediaByIDUseCase = Objects.requireNonNull(findMediaByIDUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public Pagination<VideoListResponseCommand> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction,
            final Set<String> castMembers,
            final Set<String> categories,
            final Set<String> genres
    ) {
        final var castMemberIDs = CollectionUtils.mapTo(castMembers, CastMemberID::with);
        final var categoriesIDs = CollectionUtils.mapTo(categories, CategoryID::from);
        final var genresIDs = CollectionUtils.mapTo(genres, GenreID::from);

        final var aQuery =
                new VideoSearchQuery(page, perPage, search, sort, direction, castMemberIDs, categoriesIDs, genresIDs);

        return VideoApiPresenter.present(this.listVideosUseCase.execute(aQuery));
    }

    @Override
    public ResponseEntity<?> createFull(
            final String aTitle,
            final String aDescription,
            final Integer launchedAt,
            final Double aDuration,
            final Boolean wasOpened,
            final Boolean wasPublished,
            final String aRating,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbFile,
            final MultipartFile thumbHalfFile
    ) {
        final var command = CreateVideoInputCommand.with(
                aTitle,
                aDescription,
                launchedAt,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                categories,
                genres,
                castMembers,
                resourceOf(videoFile),
                resourceOf(trailerFile),
                resourceOf(bannerFile),
                resourceOf(thumbFile),
                resourceOf(thumbHalfFile)
        );

        final var output = this.createVideoUseCase.execute(command);
        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoRequestCommand payload) {
        final var command = CreateVideoInputCommand.with(
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final var output = this.createVideoUseCase.execute(command);
        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public VideoResponseCommand getById(final String id) {
        final var command = FindVideoByIDInputCommand.with(id);
        return VideoApiPresenter.present(this.findVideoByIdUseCase.execute(command));
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateVideoRequestCommand payload) {
        final var command = UpdateVideoInputCommand.with(
                id,
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final var output = this.updateVideoUseCase.execute(command);
        return ResponseEntity.ok()
                .location(URI.create("/videos/" + output.id()))
                .body(VideoApiPresenter.present(output));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteVideoUseCase.execute(id);
    }

    @Override
    public ResponseEntity<byte[]> getMediaByType(final String id, final String type) {
        final var command = FindMediaByIDInputCommand.with(id, type);
        final var media = this.findMediaByIDUseCase.execute(command);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(media.contentType()))
                .contentLength(media.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(media.name()))
                .body(media.content());
    }

    @Override
    public ResponseEntity<?> uploadMediaByType(final String id, final String type, final MultipartFile media) {
        final var videoMediaType = VideoMediaType.of(type)
                .orElseThrow(() -> NotificationException.with(
                        new ValidationError("Invalid %s for VideoMediaType".formatted(type))));

        final var command = UploadMediaInputCommand.with(id, VideoResource.with(videoMediaType, resourceOf(media)));
        final var output = this.uploadMediaUseCase.execute(command);

        return ResponseEntity
                .created(URI.create("/videos/%s/medias/%s".formatted(id, videoMediaType)))
                .body(VideoApiPresenter.present(output));
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) {
            return null;
        }

        try {
            return Resource.with(
                    part.getBytes(),
                    HashingUtils.checksum(part.getBytes()),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
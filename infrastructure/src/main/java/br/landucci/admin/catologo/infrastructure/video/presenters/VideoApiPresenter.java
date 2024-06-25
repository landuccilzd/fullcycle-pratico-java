package br.landucci.admin.catologo.infrastructure.video.presenters;

import br.landucci.admin.catologo.application.video.find.FindVideoByIDOutputCommand;
import br.landucci.admin.catologo.application.video.list.ListVideoOutputCommand;
import br.landucci.admin.catologo.application.video.media.upload.UploadMediaOutputCommand;
import br.landucci.admin.catologo.application.video.update.UpdateVideoOutputCommand;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.video.ImageMedia;
import br.landucci.admin.catologo.domain.video.VideoMedia;
import br.landucci.admin.catologo.infrastructure.video.models.*;

public interface VideoApiPresenter {

    static VideoResponseCommand present(final FindVideoByIDOutputCommand output) {
        return new VideoResponseCommand(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating().getName(),
                output.createdAt(),
                output.updatedAt(),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                present(output.video()),
                present(output.trailer()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static VideoMediaResponseCommand present(final VideoMedia media) {
        if (media == null) {
            return null;
        }

        return new VideoMediaResponseCommand(media.getId(), media.getChecksum(), media.getName(),
                media.getRawLocation(), media.getEncodedLocation(), media.getStatus().name());
    }

    static ImageMediaResponseCommand present(final ImageMedia image) {
        if (image == null) {
            return null;
        }

        return new ImageMediaResponseCommand(image.getId(), image.getChecksum(), image.getName(), image.getLocation());
    }

    static UpdateVideoResponseCommand present(final UpdateVideoOutputCommand output) {
        return new UpdateVideoResponseCommand(output.id());
    }

    static VideoListResponseCommand present(final ListVideoOutputCommand output) {
        return new VideoListResponseCommand(output.id(), output.title(), output.description(), output.createdAt(),
                output.updatedAt());
    }

    static Pagination<VideoListResponseCommand> present(final Pagination<ListVideoOutputCommand> page) {
        return page.map(VideoApiPresenter::present);
    }

    static UploadMediaResponseCommand present(final UploadMediaOutputCommand output) {
        return new UploadMediaResponseCommand(output.videoId(), output.mediaType());
    }

}
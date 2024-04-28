package br.landucci.admin.catologo.application.video.media.upload;

import br.landucci.admin.catologo.domain.video.Video;
import br.landucci.admin.catologo.domain.video.VideoMediaType;

public record UploadMediaOutputCommand(String videoId, VideoMediaType mediaType) {

    public static UploadMediaOutputCommand with(final Video video, final VideoMediaType type) {
        return new UploadMediaOutputCommand(video.getId().getValue(), type);
    }

}
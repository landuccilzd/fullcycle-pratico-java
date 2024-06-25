package br.landucci.admin.catologo.application.video.media.upload;

import br.landucci.admin.catologo.domain.video.VideoResource;

public record UploadMediaInputCommand(String videoId, VideoResource videoResource) {

    public static UploadMediaInputCommand with(final String id, final VideoResource resource) {
        return new UploadMediaInputCommand(id, resource);
    }
}

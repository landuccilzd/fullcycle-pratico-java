package br.landucci.admin.catologo.application.video.media.update;

import br.landucci.admin.catologo.domain.video.MediaStatus;

public record UpdateMediaStatusInputCommand(MediaStatus status, String videoId, String resourceId, String folder,
        String filename) {

    public static UpdateMediaStatusInputCommand with(final MediaStatus status, final String videoId,
            final String resourceId, final String folder, final String filename) {

        return new UpdateMediaStatusInputCommand(status, videoId, resourceId, folder, filename);
    }
}

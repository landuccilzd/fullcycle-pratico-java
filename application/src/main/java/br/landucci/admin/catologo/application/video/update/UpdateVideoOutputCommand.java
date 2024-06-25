package br.landucci.admin.catologo.application.video.update;

import br.landucci.admin.catologo.domain.video.Video;

public record UpdateVideoOutputCommand(String id) {

    public static UpdateVideoOutputCommand from(final Video aVideo) {
        return new UpdateVideoOutputCommand(aVideo.getId().getValue());
    }
}

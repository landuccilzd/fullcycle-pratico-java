package br.landucci.admin.catologo.application.video.create;

import br.landucci.admin.catologo.domain.video.Video;

public record CreateVideoOutputCommand(String id) {

    public static CreateVideoOutputCommand from(final Video video) {
        return new CreateVideoOutputCommand(video.getId().getValue());
    }
}

package br.landucci.admin.catologo.application.video.media.find;

public record FindMediaByIDInputCommand(String videoId, String mediaType) {

    public static FindMediaByIDInputCommand with(final String anId, final String aType) {
        return new FindMediaByIDInputCommand(anId, aType);
    }
}

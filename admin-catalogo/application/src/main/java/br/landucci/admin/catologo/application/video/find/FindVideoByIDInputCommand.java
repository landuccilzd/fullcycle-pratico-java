package br.landucci.admin.catologo.application.video.find;


public record FindVideoByIDInputCommand(String id) {

    public static FindVideoByIDInputCommand with(final String id) {
        return new FindVideoByIDInputCommand(id);
    }
}

package br.landucci.admin.catologo.infrastructure.genre.presentetrs;

import br.landucci.admin.catologo.application.genre.find.FindByIdGenreOutputCommand;
import br.landucci.admin.catologo.application.genre.list.ListGenreOutputCommand;
import br.landucci.admin.catologo.infrastructure.genre.models.FindGenreByIdResponseCommand;
import br.landucci.admin.catologo.infrastructure.genre.models.ListGenresResponseCommand;

public interface GenreAPIPresenter {

    static FindGenreByIdResponseCommand present(FindByIdGenreOutputCommand output) {
        return new FindGenreByIdResponseCommand(output.id(), output.name(), output.isActive(), output.categories(),
                output.createdAt(), output.updatedAt(), output.deletedAt()
        );
    }

    static ListGenresResponseCommand present(final ListGenreOutputCommand output) {
        return new ListGenresResponseCommand(output.id(), output.name(), output.isActive(), output.categories(),
                output.createdAt(), output.updatedAt(), output.deletedAt());
    }
}

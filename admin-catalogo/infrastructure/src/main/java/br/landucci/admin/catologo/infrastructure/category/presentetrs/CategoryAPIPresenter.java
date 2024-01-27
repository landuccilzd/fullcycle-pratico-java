package br.landucci.admin.catologo.infrastructure.category.presentetrs;

import br.landucci.admin.catologo.application.category.find.FindByIDCategoryOutputCommand;
import br.landucci.admin.catologo.application.category.list.ListCategoriesOutputCommand;
import br.landucci.admin.catologo.infrastructure.category.models.FindByIdCategoryResponseCommand;
import br.landucci.admin.catologo.infrastructure.category.models.ListCategoriesResponseCommand;

public interface CategoryAPIPresenter {

    static FindByIdCategoryResponseCommand present(FindByIDCategoryOutputCommand output) {
        return new FindByIdCategoryResponseCommand(output.id().getValue(), output.name(), output.description(),
                output.active(), output.createdAt(), output.updatedAt(), output.deletedAt()
        );
    }

    static ListCategoriesResponseCommand present(final ListCategoriesOutputCommand output) {
        return new ListCategoriesResponseCommand(output.id().getValue(), output.name(), output.description(),
                output.active(), output.createdAt(), output.updatedAt(), output.deletedAt()
        );
    }
}

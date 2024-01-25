package br.landucci.admin.catologo.application.category.delete;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryJpaEntity;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;
    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenAValidInput_whenDeletingACategory_thenShouldDelete() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);
        final var expectedId = category.getId();

        Assertions.assertEquals(0, this.repository.count());

        final var entity = CategoryJpaEntity.from(category);
        this.repository.saveAndFlush(entity);

        Assertions.assertEquals(1, this.repository.count());

        final var input = DeleteCategoryInputCommand.with(expectedId.getValue());
        Assertions.assertDoesNotThrow(() -> useCase.execute(input));

        Assertions.assertEquals(0, this.repository.count());
    }

}

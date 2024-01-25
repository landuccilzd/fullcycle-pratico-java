package br.landucci.admin.catologo.application.category.find;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryJpaEntity;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@IntegrationTest
public class FindByIDCategoryUseCaseIT {

    @Autowired
    private FindByIDCategoryUseCase useCase;
    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenAValidInput_whenFindingACategory_thenShouldReturnTheCategory() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);
        final var expectedId = category.getId();

        Assertions.assertEquals(0, this.repository.count());

        final var entity = CategoryJpaEntity.from(category);
        this.repository.saveAndFlush(entity);

        Assertions.assertEquals(1, this.repository.count());

        final var input = FindByIDCategoryInputCommand.with(expectedId.getValue());
        final var output = useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedActive, output.active());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
        Assertions.assertNull(output.deletedAt());
    }
}

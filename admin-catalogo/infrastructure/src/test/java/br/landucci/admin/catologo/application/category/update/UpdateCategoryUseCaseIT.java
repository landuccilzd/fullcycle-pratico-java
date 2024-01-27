package br.landucci.admin.catologo.application.category.update;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryJpaEntity;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class UpdateCategoryUseCaseIT {
    @Autowired
    private UpdateCategoryUseCase useCase;
    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenAValidCommand_whenUpdateACategory_thenShouldReturnAnUpdatedCategory() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);
        final var expectedId = category.getId();

        Assertions.assertEquals(0, this.repository.count());

        final var entity = CategoryJpaEntity.from(category);
        this.repository.saveAndFlush(entity);

        Assertions.assertEquals(1, this.repository.count());

        final var command = UpdateCategoryInputCommand.with(
                expectedId.getValue(), expectedName, expectedDescription, expectedActive);
        final var output = useCase.execute(command).get();

        Assertions.assertEquals(1, this.repository.count());

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var foundEntity = repository.findById(output.id()).get();

        Assertions.assertEquals(expectedId.getValue(), foundEntity.getId());
        Assertions.assertEquals(expectedName, foundEntity.getName());
        Assertions.assertEquals(expectedDescription, foundEntity.getDescription());
        Assertions.assertEquals(expectedActive, foundEntity.isActive());
        Assertions.assertNotNull(foundEntity.getCreatedAt());
        Assertions.assertNotNull(foundEntity.getUpdatedAt());
        Assertions.assertNull(foundEntity.getDeletedAt());
    }

}
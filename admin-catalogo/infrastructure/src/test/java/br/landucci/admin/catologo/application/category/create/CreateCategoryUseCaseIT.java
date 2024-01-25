package br.landucci.admin.catologo.application.category.create;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Objects;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase useCase;
    @Autowired
    private CategoryRepository repository;
    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidCommand_whenCreateACategory_thenShouldReturnACreatedCategory() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;

        Assertions.assertEquals(0, repository.count());

        final var input = CreateCategoryInputCommand.with(expectedName, expectedDescription, expectedActive);
        final var categoryOutput = useCase.execute(input).get();

        Assertions.assertEquals(1, repository.count());

        Assertions.assertNotNull(categoryOutput);
        Assertions.assertNotNull(categoryOutput.id());

        final var categoryEntity = repository.findById(categoryOutput.id()).get();

        Assertions.assertNotNull(categoryEntity);
        Assertions.assertEquals(expectedName, categoryEntity.getName());
        Assertions.assertEquals(expectedDescription, categoryEntity.getDescription());
        Assertions.assertEquals(expectedActive, categoryEntity.isActive());
        Assertions.assertNotNull(categoryEntity.getCreatedAt());
        Assertions.assertNotNull(categoryEntity.getUpdatedAt());
        Assertions.assertNull(categoryEntity.getDeletedAt());
    }

    @Test
    public void givenAnInValidNameCommand_whenCreateACategory_thenShouldReturnDomainException() {
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var expectedErrorMessage = "Name should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, repository.count());

        final var input = CreateCategoryInputCommand.with(null, expectedDescription, expectedActive);
        final var notification = useCase.execute(input).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Assertions.assertEquals(0, repository.count());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAValidCommand_whenCreateACategoryInactivated_thenShouldReturnACreatedCategoryInactivated() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = false;

        Assertions.assertEquals(0, repository.count());

        final var input = CreateCategoryInputCommand.with(expectedName, expectedDescription, expectedActive);
        final var categoryOutput = useCase.execute(input).get();

        Assertions.assertEquals(1, repository.count());

        Assertions.assertNotNull(categoryOutput);
        Assertions.assertNotNull(categoryOutput.id());

        final var categoryEntity = repository.findById(categoryOutput.id()).get();

        Assertions.assertNotNull(categoryEntity);
        Assertions.assertEquals(expectedName, categoryEntity.getName());
        Assertions.assertEquals(expectedDescription, categoryEntity.getDescription());
        Assertions.assertEquals(expectedActive, categoryEntity.isActive());
        Assertions.assertNotNull(categoryEntity.getCreatedAt());
        Assertions.assertNotNull(categoryEntity.getUpdatedAt());
        Assertions.assertNotNull(categoryEntity.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsAnException_thenShouldReturnAnException() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = false;
        final var expectedErrorMessage = "Gateway Generic Error";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, repository.count());

        final var input = CreateCategoryInputCommand.with(expectedName, expectedDescription, expectedActive);

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(gateway).create(Mockito.any());

        final var notification = useCase.execute(input).getLeft();

        Assertions.assertEquals(0, repository.count());

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }
}
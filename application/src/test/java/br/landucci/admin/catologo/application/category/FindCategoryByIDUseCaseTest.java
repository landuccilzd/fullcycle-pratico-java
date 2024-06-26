package br.landucci.admin.catologo.application.category;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.category.find.DefaultFindCategoryByIDUseCase;
import br.landucci.admin.catologo.application.category.find.FindByIDCategoryInputCommand;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class FindCategoryByIDUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultFindCategoryByIDUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    void givenAValidInput_whenFindingACategory_thenShouldReturnTheCategory() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);
        final var expectedId = category.getId();

        Mockito.when(gateway.findById(expectedId)).thenReturn(Optional.of(Category.clone(category)));

        final var input = FindByIDCategoryInputCommand.with(expectedId.getValue());
        final var output = useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedActive, output.active());
        Assertions.assertEquals(category.getCreatedAt(), output.createdAt());
        Assertions.assertEquals(category.getUpdatedAt(), output.updatedAt());
        Assertions.assertEquals(category.getDeletedAt(), output.deletedAt());

        Mockito.verify(gateway, Mockito.times(1)).findById(expectedId);
    }

    @Test
    void givenAnInvalidID_whenDeletingACategory_thenShouldReturnNotFound() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        Mockito.when(gateway.findById(expectedId)).thenReturn(Optional.empty());

        final var input = FindByIDCategoryInputCommand.with(expectedId.getValue());
        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(input));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());

        Mockito.verify(gateway, Mockito.times(1)).findById(expectedId);
    }

    @Test
    void givenAValidInput_whenGatewayThrowsAnException_thenShouldReturnAnException() {
        final var category = Category.newCategory("Ficção Científica", "Filmes de ficção científica", true);
        final var expectedId = category.getId();
        final var expectedErrorMessage = "Gateway Generic Error";

        Mockito.when(gateway.findById(expectedId)).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var input = FindByIDCategoryInputCommand.with(expectedId.getValue());
        final var exception = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(input));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

}
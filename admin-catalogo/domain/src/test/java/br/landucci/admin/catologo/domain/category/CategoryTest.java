package br.landucci.admin.catologo.domain.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateACategory() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes que abordam ficções científicas";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
    }

    @Test
    public void givenAnInvalidNullName_whenValidateNewCatewgory_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedDescription = "Filmes que abordam ficções científicas";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name should not be null";
        final var expectedErrorCount = 1;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var exception = Assertions.assertThrows(DomainException.class, () -> category.validate());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).getMessage());
    }
}

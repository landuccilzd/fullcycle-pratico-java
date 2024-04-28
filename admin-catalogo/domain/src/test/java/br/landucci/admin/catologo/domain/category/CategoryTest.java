package br.landucci.admin.catologo.domain.category;

import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CategoryTest {

    @Test
    void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
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

    @ParameterizedTest
    @CsvSource({
            ",A Categoria mais assistida,true,1,Name should not be null",
            "empty,A Categoria mais assistida,true,1,Name should not be empty",
            "ab,A Categoria mais assistida,true,1,Name must have between 3 and 255 characters"
    })
    void givenAnInvalidNullName_whenValidateNewCatewgory_thenShouldReceiveError(final String expectedName,
            final String expectedDescription, final boolean expectedIsActive, final int expectedErrorCount,
            final String expectedErrorMessage) {

        final var name = "empty".equals(expectedName) ? "" : expectedName;
        final var category = Category.newCategory(name, expectedDescription, expectedIsActive);

        final var handler =  new ThrowsValidationHandler();
        final var exception = Assertions.assertThrows(DomainException.class, () -> category.validate(handler));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan255_whenValidateNewCatewgory_thenShouldReceiveError() {
        final String expectedName = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        final var expectedDescription = "Filmes que abordam ficções científicas";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name must have between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var handler = new ThrowsValidationHandler();
        final var exception = Assertions.assertThrows(DomainException.class, () -> category.validate(handler));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAValidBlankDescription_whenValidateNewCatewgory_thenShouldNotReceiveError() {
        final String expectedName = "Ficção Científica";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
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
    void givenAValidFalseActive_whenValidateNewCatewgory_thenShouldNotReceiveError() {
        final String expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedIsActive = false;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNotNull(category.getDeletedAt());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
    }

    @Test
    void givenAValidActiveCategory_whenDeactivate_thenReturnCategoryInactivated() {
        final String expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var updatedAt = category.getUpdatedAt();

        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());

        final var updatedCategory = category.deactivate();
        Assertions.assertDoesNotThrow(() -> updatedCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(updatedCategory);
        Assertions.assertNotNull(updatedCategory.getId());
        Assertions.assertNotNull(updatedCategory.getCreatedAt());
        Assertions.assertNotNull(updatedCategory.getUpdatedAt());
        Assertions.assertNotNull(updatedCategory.getDeletedAt());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertFalse(updatedCategory.isActive());
    }

    @Test
    void givenAValidInactiveCategory_whenActivate_thenReturnCategoryActivated() {
        final String expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedIsActive = false;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNotNull(category.getDeletedAt());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());

        final var updatedCategory = category.activate();
        Assertions.assertDoesNotThrow(() -> updatedCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(updatedCategory);
        Assertions.assertNotNull(updatedCategory.getId());
        Assertions.assertNotNull(updatedCategory.getCreatedAt());
        Assertions.assertNotNull(updatedCategory.getUpdatedAt());
        Assertions.assertNull(updatedCategory.getDeletedAt());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertTrue(updatedCategory.isActive());
    }

    @Test
    void givenAValidCategory_whenUpdateName_thenReturnCategoryUpdatedName() {
        final String expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Categoria", expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
        Assertions.assertEquals("Categoria", category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());

        final var updatedCategory = category.updateName(expectedName);

        Assertions.assertNotNull(updatedCategory);
        Assertions.assertNotNull(updatedCategory.getId());
        Assertions.assertNotNull(updatedCategory.getCreatedAt());
        Assertions.assertNotNull(updatedCategory.getUpdatedAt());
        Assertions.assertNull(updatedCategory.getDeletedAt());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
    }

    @Test
    void givenAValidCategory_whenUpdateDescription_thenReturnCategoryUpdatedDescription() {
        final String expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, "Vamos mudar isso aqui", expectedIsActive);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

         Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals("Vamos mudar isso aqui", category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());

        final var updatedCategory = category.updateDescription(expectedDescription);

        Assertions.assertNotNull(updatedCategory);
        Assertions.assertNotNull(updatedCategory.getId());
        Assertions.assertNotNull(updatedCategory.getCreatedAt());
        Assertions.assertNotNull(updatedCategory.getUpdatedAt());
        Assertions.assertNull(updatedCategory.getDeletedAt());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
    }
}
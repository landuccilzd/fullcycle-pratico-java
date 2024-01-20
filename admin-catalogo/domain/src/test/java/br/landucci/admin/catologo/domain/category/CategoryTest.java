package br.landucci.admin.catologo.domain.category;

import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
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
        final var expectedDescription = "Filmes que abordam ficções científicas";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name should not be null";
        final var expectedErrorCount = 1;

        final var category = Category.newCategory(null, expectedDescription, expectedIsActive);
        final var exception = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenValidateNewCatewgory_thenShouldReceiveError() {
        final String expectedName = "";
        final var expectedDescription = "Filmes que abordam ficções científicas";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name should not be empty";
        final var expectedErrorCount = 1;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var exception = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthLessThan3_whenValidateNewCatewgory_thenShouldReceiveError() {
        final String expectedName = "12 ";
        final var expectedDescription = "Filmes que abordam ficções científicas";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name must have between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var exception = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthMoreThan255_whenValidateNewCatewgory_thenShouldReceiveError() {
        final String expectedName = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        final var expectedDescription = "Filmes que abordam ficções científicas";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name must have between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var exception = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidBlankDescription_whenValidateNewCatewgory_thenShouldNotReceiveError() {
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
    public void givenAValidFalseActive_whenValidateNewCatewgory_thenShouldNotReceiveError() {
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
    public void givenAValidActiveCategory_whenDeactivate_thenReturnCategoryInactivated() {
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
    public void givenAValidInactiveCategory_whenActivate_thenReturnCategoryActivated() {
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
    public void givenAValidCategory_whenUpdateName_thenReturnCategoryUpdatedName() {
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
    public void givenAValidCategory_whenUpdateDescription_thenReturnCategoryUpdatedDescription() {
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
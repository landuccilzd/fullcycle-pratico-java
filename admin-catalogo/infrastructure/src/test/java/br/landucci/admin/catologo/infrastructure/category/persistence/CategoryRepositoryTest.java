package br.landucci.admin.catologo.infrastructure.category.persistence;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenAnInvalidNullName_whenCreatingACategory_thenShouldReturnAnError() {
        final var expectedPropertyName = "name";
        final var expectedErrorMessage = "not-null property references a null or transient value : br.landucci.admin.catologo.infrastructure.category.persistence.CategoryJpaEntity.name";

        final var category = Category.newCategory("Ficção científica", "Filmes de ficção científica", true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setName(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
        final var couseException = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(expectedErrorMessage, couseException.getMessage());
        Assertions.assertEquals(expectedPropertyName, couseException.getPropertyName());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCreatingACategory_thenShouldReturnAnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : br.landucci.admin.catologo.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

        final var category = Category.newCategory("Ficção científica", "Filmes de ficção científica", true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setCreatedAt(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
        final var couseException = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(expectedErrorMessage, couseException.getMessage());
        Assertions.assertEquals(expectedPropertyName, couseException.getPropertyName());
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCreatingACategory_thenShouldReturnAnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : br.landucci.admin.catologo.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

        final var category = Category.newCategory("Ficção científica", "Filmes de ficção científica", true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setUpdatedAt(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
        final var couseException = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(expectedErrorMessage, couseException.getMessage());
        Assertions.assertEquals(expectedPropertyName, couseException.getPropertyName());
    }
}

package br.landucci.admin.catologo.domain.genre;

import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GenreTest {

    @Test
    public void givenAValidInput_whenCreatingGenre_thenShouldInstantiateAGenre() {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryCount = 0;

        final var genre = Genre.newGenre(expectedName, expectedActive);

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedActive, genre.isActive());
        Assertions.assertEquals(expectedCategoryCount, genre.categoriesCount());
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertNotNull(genre.getUpdatedAt());
        Assertions.assertNull(genre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullNameInput_whenCreatingGenre_thenShouldReturnAnError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> Genre.newGenre(null, true));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnInvalidEmptyNameInput_whenCreatingGenre_thenShouldReturnAnError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be empty";

        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> Genre.newGenre("", true));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnInvalidShortNameInput_whenCreatingGenre_thenShouldReturnAnError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must have between 3 and 255 characters";

        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> Genre.newGenre("ab", true));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnInvalidLongNameInput_whenCreatingGenre_thenShouldReturnAnError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must have between 3 and 255 characters";
        final var expectedName = """
            But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and
            I will give you a complete account of the system, and expound the actual teachings of the great explorer of
            the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself,
            because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter
            consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain
            pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can
            procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical
            exercise, except to obtain some advantage from it? But who has any right to find fault with a man who
            chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no
            resultant pleasure?
        """;

        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> Genre.newGenre(expectedName, true));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnActiveGenre_whenInactivating_thenShouldInactiveTheGenre() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = false;
        final var expectedCategoryCount = 0;

        final var genre = Genre.newGenre(expectedName, true);
        Assertions.assertNotNull(genre);
        Assertions.assertTrue(genre.isActive());
        Assertions.assertNull(genre.getDeletedAt());

        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        genre.deactivate();

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedActive, genre.isActive());
        Assertions.assertEquals(expectedCategoryCount, genre.categoriesCount());
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(genre.getUpdatedAt()));
        Assertions.assertNotNull(genre.getDeletedAt());
    }

    @Test
    public void givenAnIncctiveGenre_whenActivating_thenShouldActiveTheGenre() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryCount = 0;

        final var genre = Genre.newGenre(expectedName, false);
        Assertions.assertNotNull(genre);
        Assertions.assertFalse(genre.isActive());
        Assertions.assertNotNull(genre.getDeletedAt());

        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        genre.activate();

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedActive, genre.isActive());
        Assertions.assertEquals(expectedCategoryCount, genre.categoriesCount());
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(genre.getUpdatedAt()));
        Assertions.assertNull(genre.getDeletedAt());
    }

    @Test
    public void givenAnExistingGenre_whenUpdating_thenShouldUpdateTheGenre() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryCount = 0;

        final var genre = Genre.newGenre("romance", false);
        Assertions.assertNotNull(genre);
        Assertions.assertFalse(genre.isActive());
        Assertions.assertNotNull(genre.getDeletedAt());

        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        genre.updateName(expectedName).activate();

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedActive, genre.isActive());
        Assertions.assertEquals(expectedCategoryCount, genre.categoriesCount());
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(genre.getUpdatedAt()));
        Assertions.assertNull(genre.getDeletedAt());
    }

    @Test
    public void givenAnExistingGenre_whenUpdatingWithNullName_thenShouldReturnNotificationException() {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        final var genre = Genre.newGenre(expectedName, expectedActive);

        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> genre.updateName(null));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnExistingGenre_whenUpdatingWithEmptyName_thenShouldReturnNotificationException() {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be empty";

        final var genre = Genre.newGenre(expectedName, expectedActive);

        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> genre.updateName(""));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnExistingGenre_whenUpdatingWithShortName_thenShouldReturnNotificationException() {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must have between 3 and 255 characters";

        final var genre = Genre.newGenre(expectedName, expectedActive);

        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> genre.updateName("ab"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnExistingGenre_whenUpdatingWithLongName_thenShouldReturnNotificationException() {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must have between 3 and 255 characters";
        final var newName = """
            But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and
            I will give you a complete account of the system, and expound the actual teachings of the great explorer of
            the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself,
            because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter
            consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain
            pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can
            procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical
            exercise, except to obtain some advantage from it? But who has any right to find fault with a man who
            chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no
            resultant pleasure?
        """;

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var exception = Assertions.assertThrows(NotificationException.class,
                () -> genre.updateName(newName));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnExistingGenre_whenAddingACategory_thenShouldUpdateTheGenre() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryCount = 1;
        final var expectedCategoryID = CategoryID.from("123");

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        genre.adicionarCategoria(expectedCategoryID);

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedActive, genre.isActive());
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(genre.getUpdatedAt()));
        Assertions.assertNull(genre.getDeletedAt());
        Assertions.assertEquals(expectedCategoryCount, genre.categoriesCount());
        Assertions.assertEquals(expectedCategoryID.getValue(), genre.getCategories().get(0).getValue());
    }

    @Test
    public void givenAnExistingGenre_whenAddingADuplicatedCategory_thenShouldReturnError() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryID = CategoryID.from("123");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID %s already exists".formatted(expectedCategoryID.getValue());

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        genre.adicionarCategoria(expectedCategoryID);
        final var exception = Assertions.assertThrows(DomainException.class,
                () -> genre.adicionarCategoria(expectedCategoryID));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnExistingGenre_whenAddingSomeCategories_thenShouldUpdateTheGenre() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryCount = 3;
        final var expectedMovieID = CategoryID.from("123");
        final var expectedSeriesID = CategoryID.from("456");
        final var expectedDocumentaryID = CategoryID.from("789");

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        genre.adicionarCategorias(List.<CategoryID>of(expectedMovieID, expectedSeriesID, expectedDocumentaryID));

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedActive, genre.isActive());
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(genre.getUpdatedAt()));
        Assertions.assertNull(genre.getDeletedAt());
        Assertions.assertEquals(expectedCategoryCount, genre.categoriesCount());
        Assertions.assertEquals(expectedMovieID.getValue(), genre.getCategories().get(0).getValue());
        Assertions.assertEquals(expectedSeriesID.getValue(), genre.getCategories().get(1).getValue());
        Assertions.assertEquals(expectedDocumentaryID.getValue(), genre.getCategories().get(2).getValue());
    }

    @Test
    public void givenAnExistingGenre_whenAddingSomeDuplicatedCategory_thenShouldReturnError()
            throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryID = CategoryID.from("123");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID %s already exists".formatted(expectedCategoryID.getValue());

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        final var categories = List.<CategoryID>of(expectedCategoryID, expectedCategoryID);
        genre.adicionarCategorias(categories);

        final var exception = Assertions.assertThrows(DomainException.class,
                () -> genre.adicionarCategorias(categories));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnExistingGenre_whenRemovingACategory_thenShouldUpdateTheGenre() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryCount = 1;
        final var expectedCategoryID = CategoryID.from("123");

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        genre.adicionarCategoria(expectedCategoryID);
        Assertions.assertEquals(expectedCategoryCount, genre.categoriesCount());
        Assertions.assertEquals(expectedCategoryID.getValue(), genre.getCategories().get(0).getValue());

        genre.removerCategoria(expectedCategoryID);
        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedActive, genre.isActive());
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(genre.getUpdatedAt()));
        Assertions.assertNull(genre.getDeletedAt());
        Assertions.assertEquals(0, genre.categoriesCount());
    }

    @Test
    public void givenAnExistingGenre_whenRemovingAnInexistingCategory_thenShouldReturnError()
            throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryID = CategoryID.from("123");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID %s does not exists".formatted(expectedCategoryID.getValue());

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        final var exception = Assertions.assertThrows(DomainException.class,
                () -> genre.removerCategoria(expectedCategoryID));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnExistingGenre_whenRemovingSomeCategories_thenShouldUpdateTheGenre() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryCount = 3;
        final var expectedMovieID = CategoryID.from("123");
        final var expectedSeriesID = CategoryID.from("456");
        final var expectedDocumentaryID = CategoryID.from("789");

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        final var categories = List.<CategoryID>of(expectedMovieID, expectedSeriesID, expectedDocumentaryID);

        genre.adicionarCategorias(categories);
        Assertions.assertEquals(expectedCategoryCount, genre.categoriesCount());
        Assertions.assertEquals(expectedMovieID.getValue(), genre.getCategories().get(0).getValue());
        Assertions.assertEquals(expectedSeriesID.getValue(), genre.getCategories().get(1).getValue());
        Assertions.assertEquals(expectedDocumentaryID.getValue(), genre.getCategories().get(2).getValue());

        genre.removerCategorias(categories);

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedActive, genre.isActive());
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(genre.getUpdatedAt()));
        Assertions.assertNull(genre.getDeletedAt());
        Assertions.assertEquals(0, genre.categoriesCount());
    }

    @Test
    public void givenAnExistingGenre_whenRemovingSomeInexistingCategory_thenShouldReturnError()
            throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedMovieID = CategoryID.from("123");
        final var expectedSeriesID = CategoryID.from("456");
        final var expectedDocumentaryID = CategoryID.from("789");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID %s does not exists".formatted(expectedMovieID.getValue());

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        final var categories = List.<CategoryID>of(expectedMovieID, expectedSeriesID, expectedDocumentaryID);
        final var exception = Assertions.assertThrows(DomainException.class,
                () -> genre.removerCategorias(categories));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    public void givenAnExistingGenre_whenRemovingAllCategories_thenShouldUpdateTheGenre() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategoryCount = 3;
        final var expectedMovieID = CategoryID.from("123");
        final var expectedSeriesID = CategoryID.from("456");
        final var expectedDocumentaryID = CategoryID.from("789");

        final var genre = Genre.newGenre(expectedName, expectedActive);
        final var updated = genre.getUpdatedAt();
        Thread.sleep(10);

        final var categories = List.<CategoryID>of(expectedMovieID, expectedSeriesID, expectedDocumentaryID);

        genre.adicionarCategorias(categories);
        Assertions.assertEquals(expectedCategoryCount, genre.categoriesCount());
        Assertions.assertEquals(expectedMovieID.getValue(), genre.getCategories().get(0).getValue());
        Assertions.assertEquals(expectedSeriesID.getValue(), genre.getCategories().get(1).getValue());
        Assertions.assertEquals(expectedDocumentaryID.getValue(), genre.getCategories().get(2).getValue());

        genre.limparCategorias();

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedActive, genre.isActive());
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(genre.getUpdatedAt()));
        Assertions.assertNull(genre.getDeletedAt());
        Assertions.assertEquals(0, genre.categoriesCount());
    }
}
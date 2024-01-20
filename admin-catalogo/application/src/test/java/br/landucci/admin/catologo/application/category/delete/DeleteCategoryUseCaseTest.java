package br.landucci.admin.catologo.application.category.delete;

import br.landucci.admin.catologo.application.category.create.CreateCategoryInputCommand;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {
    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenAValidInput_whenDeletingACategory_thenShouldDelete() {
        final var category = Category.newCategory("Ficção Científica", "Filmes de ficção científica", true);
        final var expectedId = category.getId();

        Mockito.when(gateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(category));

        final var input = DeleteCategoryInputCommand.with(expectedId.getValue());
        Assertions.assertDoesNotThrow(() -> useCase.execute(input));

        Mockito.verify(gateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAnInvalidID_whenDeletingACategory_thenShouldReturnNotFound() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var input = DeleteCategoryInputCommand.with(expectedId.getValue());
        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(input));

        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(gateway, Mockito.times(0)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidInput_whenGatewayThrowsAnException_thenShouldReturnAnException() {
        final var category = Category.newCategory("Ficção Científica", "Filmes de ficção científica", true);
        final var expectedId = category.getId();
        final var expectedErrorMessage = "Gateway Generic Error";
        final var expectedErrorCount = 1;

        final var input = DeleteCategoryInputCommand.with(expectedId.getValue());
        Assertions.assertThrows(DomainException.class, () -> useCase.execute(input));
    }
}
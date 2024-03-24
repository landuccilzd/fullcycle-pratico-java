package br.landucci.admin.catologo.application.category;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.category.update.DefaultUpdateCategoryUseCase;
import br.landucci.admin.catologo.application.category.update.UpdateCategoryInputCommand;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UpdateCategoryUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenAValidCommand_whenUpdateACategory_thenShouldReturnAnUpdatedCategory() {
        final var category = Category.newCategory("Comédia", "", true);
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var expectedCategoryId = category.getId();

        final var command = UpdateCategoryInputCommand.with(
                expectedCategoryId.getValue(), expectedName, expectedDescription, expectedActive);

        Mockito.when(gateway.findById(Mockito.eq(expectedCategoryId))).thenReturn(Optional.of(category.clone()));
        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.eq(expectedCategoryId));
        Mockito.verify(gateway, Mockito.times(1)).update(Mockito.argThat(cat -> {
            return Objects.nonNull(cat.getId()) &&
                    Objects.equals(expectedName, cat.getName()) &&
                    Objects.equals(expectedDescription, cat.getDescription()) &&
                    Objects.equals(expectedActive, cat.isActive()) &&
                    Objects.equals(category.getCreatedAt(), cat.getCreatedAt()) &&
                    category.getUpdatedAt().isBefore(cat.getUpdatedAt()) &&
                    Objects.isNull(cat.getDeletedAt());
        }));
    }

    @Test
    //Teste com propriedade inválida
    public void givenAnInValidNameNullCommand_whenUpdateACategory_thenShouldReturnDomainException() {
        final var category = Category.newCategory("Comédia", "Filmes de ficção comédia", true);
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var expectedCategoryId = category.getId();

        final var expectedErrorMessage = "Name should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryInputCommand.with(expectedCategoryId.getValue(), null, expectedDescription, expectedActive);

        Mockito.when(gateway.findById(Mockito.eq(expectedCategoryId))).thenReturn(Optional.of(category.clone()));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    //Teste com propriedade inválida
    public void givenAnInValidNameEmptyCommand_whenUpdateACategory_thenShouldReturnDomainException() {
        final var category = Category.newCategory("Comédia", "Filmes de ficção comédia", true);
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var expectedCategoryId = category.getId();

        final var expectedErrorMessage = "Name should not be empty";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryInputCommand.with(expectedCategoryId.getValue(), "", expectedDescription, expectedActive);

        Mockito.when(gateway.findById(Mockito.eq(expectedCategoryId))).thenReturn(Optional.of(category.clone()));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

//    @Test
//    public void givenAValidCommand_whenInactivateACategory_thenShouldReturnAnInactivatedCatgory() {
//        final var category = Category.newCategory("Comédia", "", true);
//        final var expectedName = "Ficção Científica";
//        final var expectedDescription = "Filmes de ficção científica";
//        final var expectedActive = false;
//        final var expectedId = category.getId();
//
//        final var input = UpdateCategoryInputCommand.with(
//                expectedId.getValue(), expectedName, expectedDescription, expectedActive);
//
//        Mockito.when(gateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(category.clone()));
//        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
//
//        final var output = useCase.execute(input).get();
//
//        Assertions.assertNotNull(output);
//        Assertions.assertNotNull(output.id());
//
//        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
//        Mockito.verify(gateway, Mockito.times(1)).update(Mockito.argThat(cat -> {
//            return Objects.nonNull(cat.getId()) &&
//                    Objects.equals(expectedName, cat.getName()) &&
//                    Objects.equals(expectedDescription, cat.getDescription()) &&
//                    Objects.equals(expectedActive, cat.isActive()) &&
//                    Objects.equals(category.getCreatedAt(), cat.getCreatedAt()) &&
//                    category.getUpdatedAt().isBefore(cat.getUpdatedAt()) &&
//                    Objects.nonNull(cat.getDeletedAt());
//        }));
//    }

//    @Test
//    public void givenAValidCommand_whenActivateACategory_thenShouldReturnAnActivatedCatgory() {
//        final var category = Category.newCategory("Comédia", "", false);
//        final var expectedName = "Ficção Científica";
//        final var expectedDescription = "Filmes de ficção científica";
//        final var expectedActive = true;
//        final var expectedId = category.getId();
//
//        final var input = UpdateCategoryInputCommand.with(
//                expectedId.getValue(), expectedName, expectedDescription, expectedActive);
//
//        Mockito.when(gateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(category.clone()));
//        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
//
//        final var output = useCase.execute(input).get();
//
//        Assertions.assertNotNull(output);
//        Assertions.assertNotNull(output.id());
//
//        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
//        Mockito.verify(gateway, Mockito.times(1)).update(Mockito.argThat(cat -> {
//            return Objects.nonNull(cat.getId()) &&
//                    Objects.equals(expectedName, cat.getName()) &&
//                    Objects.equals(expectedDescription, cat.getDescription()) &&
//                    Objects.equals(expectedActive, cat.isActive()) &&
//                    Objects.equals(category.getCreatedAt(), cat.getCreatedAt()) &&
//                    category.getUpdatedAt().isBefore(cat.getUpdatedAt()) &&
//                    Objects.isNull(cat.getDeletedAt());
//        }));
//    }

    @Test
    //Criar uma categoria simulando um erro vindo do gateway
    public void givenAValidCommand_whenGatewayThrowsAnException_thenShouldReturnAnException() {
        final var category = Category.newCategory("Comédia", "", true);
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var expectedId = category.getId();
        final var expectedErrorMessage = "Gateway Generic Error";
        final var expectedErrorCount = 1;

        Mockito.when(gateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(category.clone()));
        Mockito.when(gateway.update(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var input = UpdateCategoryInputCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedActive);
        final var notification = useCase.execute(input).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(1)).update(Mockito.argThat(cat -> {
            return Objects.equals(expectedName, cat.getName()) &&
                    Objects.equals(expectedDescription, cat.getDescription()) &&
                    Objects.equals(expectedActive, cat.isActive()) &&
                    Objects.nonNull(cat.getId()) &&
                    Objects.nonNull(cat.getCreatedAt()) &&
                    Objects.nonNull(cat.getUpdatedAt()) &&
                    Objects.isNull(cat.getDeletedAt());
        }));
    }

    @Test
    public void givenACommandWithInvalidID_whenUpdateACategory_thenShouldReturnNotFound() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryInputCommand.with(expectedId, expectedName, expectedDescription, expectedActive);

        Mockito.when(gateway.findById(Mockito.eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command).getLeft());

        Assertions.assertEquals(expectedErrorCount, exception.getErrorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.eq(CategoryID.from(expectedId)));
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }
}

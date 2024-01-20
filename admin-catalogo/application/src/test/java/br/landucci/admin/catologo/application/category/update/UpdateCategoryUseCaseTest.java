package br.landucci.admin.catologo.application.category.update;

import br.landucci.admin.catologo.application.category.create.CreateCategoryInputCommand;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
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
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;
    @Mock
    private CategoryGateway gateway;

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
    public void givenAnInValidNameCommand_whenUpdateACategory_thenShouldReturnDomainException() {
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
    public void givenAValidCommand_whenInctivateACategory_thenShouldReturnAnIncctivatedCatgory() {
        final var category = Category.newCategory("Comédia", "", true);
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = false;
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
            return Objects.equals(expectedCategoryId.getValue(), cat.getId()) &&
                    Objects.equals(expectedName, cat.getName()) &&
                    Objects.equals(expectedDescription, cat.getDescription()) &&
                    Objects.equals(expectedActive, cat.isActive()) &&
                    Objects.equals(category.getCreatedAt(), cat.getCreatedAt()) &&
                    category.getUpdatedAt().isBefore(cat.getUpdatedAt()) &&
                    Objects.nonNull(cat.getDeletedAt());
        }));
    }
}

package br.landucci.admin.catologo.application.category;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.category.create.CreateCategoryInputCommand;
import br.landucci.admin.catologo.application.category.create.DefaultCreateCategoryUseCase;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

public class CreateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    //Caminho feliz
    @Test
    public void givenAValidCommand_whenCreateACategory_thenShouldReturnACreatedCategory() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = CreateCategoryInputCommand.with(expectedName, expectedDescription, expectedActive);
        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.argThat(category -> {
            return Objects.equals(expectedName, category.getName()) &&
                    Objects.equals(expectedDescription, category.getDescription()) &&
                    Objects.equals(expectedActive, category.isActive()) &&
                    Objects.nonNull(category.getId()) &&
                    Objects.nonNull(category.getCreatedAt()) &&
                    Objects.nonNull(category.getUpdatedAt()) &&
                    Objects.isNull(category.getDeletedAt());
        }));
    }

    @Test
    //Teste com propriedade inválida
    public void givenAnInValidNameCommand_whenCreateACategory_thenShouldReturnDomainException() {
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var expectedErrorMessage = "Name should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryInputCommand.with(null, expectedDescription, expectedActive);
        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    //Criar categoria inativa
    public void givenAValidCommand_whenCreateACategoryInactivated_thenShouldReturnACreatedCategoryInactivated() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = false;

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = CreateCategoryInputCommand.with(expectedName, expectedDescription, expectedActive);
        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.argThat(category -> {
            return Objects.equals(expectedName, category.getName()) &&
                    Objects.equals(expectedDescription, category.getDescription()) &&
                    Objects.equals(expectedActive, category.isActive()) &&
                    Objects.nonNull(category.getId()) &&
                    Objects.nonNull(category.getCreatedAt()) &&
                    Objects.nonNull(category.getUpdatedAt()) &&
                    Objects.nonNull(category.getDeletedAt());
        }));
    }

    @Test
    //Criar uma categoria simulando um erro vindo do gateway
    public void givenAValidCommand_whenGatewayThrowsAnException_thenShouldReturnAnException() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = false;
        final var expectedErrorMessage = "Gateway Generic Error";
        final var expectedErrorCount = 1;

        Mockito.when(gateway.create(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var command = CreateCategoryInputCommand.with(expectedName, expectedDescription, expectedActive);
        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.argThat(category -> {
            return Objects.equals(expectedName, category.getName()) &&
                    Objects.equals(expectedDescription, category.getDescription()) &&
                    Objects.equals(expectedActive, category.isActive()) &&
                    Objects.nonNull(category.getId()) &&
                    Objects.nonNull(category.getCreatedAt()) &&
                    Objects.nonNull(category.getUpdatedAt()) &&
                    Objects.nonNull(category.getDeletedAt());
        }));
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

}
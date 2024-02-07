package br.landucci.admin.catologo.application.genre;

import br.landucci.admin.catologo.application.genre.create.CreateGenreInputCommand;
import br.landucci.admin.catologo.application.genre.create.DefaultCreateGenreUseCase;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;
    @Mock
    private GenreGateway gateway;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenAValidCommand_whenCreateAGenre_thenShouldReturnACreatedGenre() {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();


        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = CreateGenreInputCommand.with(expectedName, expectedActive, asString(expectedCategories));

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.argThat(genre -> {
            return Objects.nonNull(genre.getId()) &&
                    Objects.equals(expectedName, genre.getName()) &&
                    Objects.equals(expectedActive, genre.isActive()) &&
                    Objects.equals(expectedCategories, genre.getCategories()) &&
                    Objects.nonNull(genre.getCreatedAt()) &&
                    Objects.nonNull(genre.getUpdatedAt()) &&
                    Objects.isNull(genre.getDeletedAt());
        }));
    }

    /*
    @Test
    public void givenAnInValidNameCommand_whenCreateAGenre_thenShouldReturnDomainException() {
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var expectedErrorMessage = "Name should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateGenreInputCommand.with(null, expectedDescription, expectedActive);
        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    //Criar categoria inativa
    public void givenAValidCommand_whenCreateAGenreInactivated_thenShouldReturnACreatedGenreInactivated() {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = false;

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = CreateGenreInputCommand.with(expectedName, expectedDescription, expectedActive);
        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.argThat(genre -> {
            return Objects.equals(expectedName, genre.getName()) &&
                    Objects.equals(expectedDescription, genre.getDescription()) &&
                    Objects.equals(expectedActive, genre.isActive()) &&
                    Objects.nonNull(genre.getId()) &&
                    Objects.nonNull(genre.getCreatedAt()) &&
                    Objects.nonNull(genre.getUpdatedAt()) &&
                    Objects.nonNull(genre.getDeletedAt());
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

        final var command = CreateGenreInputCommand.with(expectedName, expectedDescription, expectedActive);
        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.argThat(genre -> {
            return Objects.equals(expectedName, genre.getName()) &&
                    Objects.equals(expectedDescription, genre.getDescription()) &&
                    Objects.equals(expectedActive, genre.isActive()) &&
                    Objects.nonNull(genre.getId()) &&
                    Objects.nonNull(genre.getCreatedAt()) &&
                    Objects.nonNull(genre.getUpdatedAt()) &&
                    Objects.nonNull(genre.getDeletedAt());
        }));
    }
*/
    private List<String> asString(final List<CategoryID> ids) {
        return ids.stream().map(CategoryID::getValue).toList();
    }

}

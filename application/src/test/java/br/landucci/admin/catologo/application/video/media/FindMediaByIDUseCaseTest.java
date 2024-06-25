package br.landucci.admin.catologo.application.video.media;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.video.media.find.DefaultFindMediaByIDUseCase;
import br.landucci.admin.catologo.application.video.media.find.FindMediaByIDInputCommand;
import br.landucci.admin.catologo.application.Fixture;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.video.MediaResourceGateway;
import br.landucci.admin.catologo.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class FindMediaByIDUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultFindMediaByIDUseCase useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway);
    }

    @Test
    void givenAVideoIdAndType_whenIsAValidCommand_thenShouldReturnResource() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        Mockito.when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.of(expectedResource));

        final var input = FindMediaByIDInputCommand.with(expectedId.getValue(), expectedType.name());
        final var output = this.useCase.execute(input);

        Assertions.assertEquals(expectedResource.getName(), output.name());
        Assertions.assertEquals(expectedResource.getContent(), output.content());
        Assertions.assertEquals(expectedResource.getContentType(), output.contentType());
    }

    @Test
    void givenAVideoIdAndType_whenIsNotFound_thenShouldReturnNotFoundException() {
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();

        Mockito.when(mediaResourceGateway.getResource(expectedId, expectedType)).thenReturn(Optional.empty());

        final var input = FindMediaByIDInputCommand.with(expectedId.getValue(), expectedType.name());

        Assertions.assertThrows(NotFoundException.class, () -> this.useCase.execute(input));
    }

    @Test
    public void givenAVideoIdAndType_whenTypeDoesntExists_thenShouldReturnNotFoundException() {
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Media type QUALQUER doesn't exists";

        final var input = FindMediaByIDInputCommand.with(expectedId.getValue(), "QUALQUER");
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> this.useCase.execute(input));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}

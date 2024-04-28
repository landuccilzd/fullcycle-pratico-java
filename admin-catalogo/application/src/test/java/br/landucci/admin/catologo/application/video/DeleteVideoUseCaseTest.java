package br.landucci.admin.catologo.application.video;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.video.delete.DefaultDeleteVideoUseCase;
import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.video.MediaResourceGateway;
import br.landucci.admin.catologo.domain.video.VideoGateway;
import br.landucci.admin.catologo.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway gateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;


    @Test
    void givenAValidId_whenDeletingAVideo_shouldDeleteIt() {
        final var expectedId = VideoID.unique();

        Mockito.doNothing().when(gateway).deleteById(Mockito.any());
        Mockito.doNothing().when(mediaResourceGateway).clearResources(Mockito.any());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway).deleteById(expectedId);
        Mockito.verify(mediaResourceGateway).clearResources(expectedId);
    }

    @Test
    void givenAnInvalidId_whenDeletingAVideo_thenShouldBeOk() {
        final var expectedId = VideoID.with("1231");

        Mockito.doNothing().when(gateway).deleteById(Mockito.any());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway).deleteById(expectedId);
    }

    @Test
    void givenAValidId_whenDeletingVideoAndGatewayThrowsException_thenShouldReceiveException() {
        final var expectedId = VideoID.with("1231");

        Mockito.doThrow(DomainException.with(new ValidationError("Error on delete video")))
                .when(gateway).deleteById(Mockito.any());

        Assertions.assertThrows(DomainException.class,() ->
                this.useCase.execute(expectedId.getValue())
        );

        Mockito.verify(gateway).deleteById(Mockito.eq(expectedId));
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway, mediaResourceGateway);
    }
}

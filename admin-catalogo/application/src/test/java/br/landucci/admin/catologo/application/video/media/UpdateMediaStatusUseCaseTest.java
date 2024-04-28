package br.landucci.admin.catologo.application.video.media;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import br.landucci.admin.catologo.application.video.media.update.UpdateMediaStatusInputCommand;
import br.landucci.admin.catologo.domain.Fixture;
import br.landucci.admin.catologo.domain.video.MediaStatus;
import br.landucci.admin.catologo.domain.video.Video;
import br.landucci.admin.catologo.domain.video.VideoGateway;
import br.landucci.admin.catologo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

class UpdateMediaStatusUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateMediaStatusUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    void givenCommandForVideo_whenIsValid_thenShouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);
        final var video = Fixture.Videos.video().updateContent(expectedMedia);
        final var expectedId = video.getId();

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(video));
        Mockito.when(videoGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var input = UpdateMediaStatusInputCommand.with(expectedStatus, expectedId.getValue(),
                expectedMedia.getId(), expectedFolder, expectedFilename);

        this.useCase.execute(input);

        Mockito.verify(videoGateway, Mockito.times(1)).findById(expectedId);

        final var captor = ArgumentCaptor.forClass(Video.class);

        Mockito.verify(videoGateway, Mockito.times(1)).update(captor.capture());

        final var capturedVideo = captor.getValue();

        Assertions.assertTrue(capturedVideo.getTrailer().isEmpty());

        final var capturedVideoMedia = capturedVideo.getContent().get();

        Assertions.assertEquals(expectedMedia.getId(), capturedVideoMedia.getId());
        Assertions.assertEquals(expectedMedia.getRawLocation(), capturedVideoMedia.getRawLocation());
        Assertions.assertEquals(expectedMedia.getChecksum(), capturedVideoMedia.getChecksum());
        Assertions.assertEquals(expectedStatus, capturedVideoMedia.getStatus());
        Assertions.assertEquals(expectedFolder.concat("/").concat(expectedFilename), capturedVideoMedia.getEncodedLocation());
    }

    @Test
    void givenCommandForVideo_whenIsValidForProcessing_thenShouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.PROCESSING;
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);
        final var video = Fixture.Videos.video().updateContent(expectedMedia);
        final var expectedId = video.getId();

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(video));
        Mockito.when(videoGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var input = UpdateMediaStatusInputCommand.with(expectedStatus, expectedId.getValue(),
                expectedMedia.getId(), null, null);

        this.useCase.execute(input);

        Mockito.verify(videoGateway, Mockito.times(1)).findById(expectedId);

        final var captor = ArgumentCaptor.forClass(Video.class);

        Mockito.verify(videoGateway, Mockito.times(1)).update(captor.capture());

        final var capturedVideo = captor.getValue();

        Assertions.assertTrue(capturedVideo.getTrailer().isEmpty());

        final var capturedVideoMedia = capturedVideo.getContent().get();

        Assertions.assertEquals(expectedMedia.getId(), capturedVideoMedia.getId());
        Assertions.assertEquals(expectedMedia.getRawLocation(), capturedVideoMedia.getRawLocation());
        Assertions.assertEquals(expectedMedia.getChecksum(), capturedVideoMedia.getChecksum());
        Assertions.assertEquals(expectedStatus, capturedVideoMedia.getStatus());
        Assertions.assertTrue(capturedVideoMedia.getEncodedLocation().isBlank());
    }

    @Test
    void givenCommandForTrailer_whenIsValid_thenShouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);
        final var video = Fixture.Videos.video().updateTrailer(expectedMedia);
        final var expectedId = video.getId();

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(video));
        Mockito.when(videoGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var input = UpdateMediaStatusInputCommand.with(expectedStatus, expectedId.getValue(),
                expectedMedia.getId(), expectedFolder, expectedFilename);

        this.useCase.execute(input);

        Mockito.verify(videoGateway, Mockito.times(1)).findById(expectedId);

        final var captor = ArgumentCaptor.forClass(Video.class);

        Mockito.verify(videoGateway, Mockito.times(1)).update(captor.capture());

        final var capturedVideo = captor.getValue();

        Assertions.assertTrue(capturedVideo.getContent().isEmpty());

        final var capturedVideoMedia = capturedVideo.getTrailer().get();

        Assertions.assertEquals(expectedMedia.getId(), capturedVideoMedia.getId());
        Assertions.assertEquals(expectedMedia.getRawLocation(), capturedVideoMedia.getRawLocation());
        Assertions.assertEquals(expectedMedia.getChecksum(), capturedVideoMedia.getChecksum());
        Assertions.assertEquals(expectedStatus, capturedVideoMedia.getStatus());
        Assertions.assertEquals(expectedFolder.concat("/").concat(expectedFilename), capturedVideoMedia.getEncodedLocation());
    }

    @Test
    void givenCommandForTrailer_whenIsValidForProcessing_thenShouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.PROCESSING;
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);
        final var video = Fixture.Videos.video().updateTrailer(expectedMedia);
        final var expectedId = video.getId();

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(video));
        Mockito.when(videoGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var input = UpdateMediaStatusInputCommand.with(expectedStatus, expectedId.getValue(),
                expectedMedia.getId(), null, null
        );

        this.useCase.execute(input);

        Mockito.verify(videoGateway, Mockito.times(1)).findById(expectedId);

        final var captor = ArgumentCaptor.forClass(Video.class);

        Mockito.verify(videoGateway, Mockito.times(1)).update(captor.capture());

        final var capturedVideo = captor.getValue();

        Assertions.assertTrue(capturedVideo.getContent().isEmpty());

        final var capturedVideoMedia = capturedVideo.getTrailer().get();

        Assertions.assertEquals(expectedMedia.getId(), capturedVideoMedia.getId());
        Assertions.assertEquals(expectedMedia.getRawLocation(), capturedVideoMedia.getRawLocation());
        Assertions.assertEquals(expectedMedia.getChecksum(), capturedVideoMedia.getChecksum());
        Assertions.assertEquals(expectedStatus, capturedVideoMedia.getStatus());
        Assertions.assertTrue(capturedVideoMedia.getEncodedLocation().isBlank());
    }

    @Test
    void givenCommandForTrailer_whenIsInvalid_thenShouldDoNothing() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);
        final var video = Fixture.Videos.video().updateTrailer(expectedMedia);
        final var expectedId = video.getId();

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(video));

        final var input = UpdateMediaStatusInputCommand.with(expectedStatus, expectedId.getValue(), "randomId",
                expectedFolder, expectedFilename);

        this.useCase.execute(input);

        Mockito.verify(videoGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

}
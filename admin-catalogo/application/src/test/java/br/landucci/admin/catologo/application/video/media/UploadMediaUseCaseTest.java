package br.landucci.admin.catologo.application.video.media;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.video.media.upload.DefaultUploadMediaUseCase;
import br.landucci.admin.catologo.application.video.media.upload.UploadMediaInputCommand;
import br.landucci.admin.catologo.domain.Fixture;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.video.MediaResourceGateway;
import br.landucci.admin.catologo.domain.video.VideoGateway;
import br.landucci.admin.catologo.domain.video.VideoMediaType;
import br.landucci.admin.catologo.domain.video.VideoResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

class UploadMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUploadMediaUseCase useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Mock
    private VideoGateway videoGateway;

    @Test
    void givenCmdToUpload_whenIsValid_thenShouldUpdateVideoMediaAndPersistIt() {
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(video));
        Mockito.when(mediaResourceGateway.storeAudioVideo(Mockito.any(), Mockito.any())).thenReturn(expectedMedia);
        Mockito.when(videoGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var input = UploadMediaInputCommand.with(expectedId.getValue(), expectedVideoResource);
        final var output = useCase.execute(input);

        Assertions.assertEquals(expectedType, output.mediaType());
        Assertions.assertEquals(expectedId.getValue(), output.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(mediaResourceGateway, Mockito.times(1)).storeAudioVideo(expectedId, expectedVideoResource);
        Mockito.verify(videoGateway, Mockito.times(1)).update(ArgumentMatchers.argThat(vid ->
                Objects.equals(expectedMedia, vid.getContent().get())
                && vid.getTrailer().isEmpty()
                && vid.getBanner().isEmpty()
                && vid.getThumbnail().isEmpty()
                && vid.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenCmdToUpload_whenIsValid_thenShouldUpdateTrailerMediaAndPersistIt() {
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(video));
        Mockito.when(mediaResourceGateway.storeAudioVideo(Mockito.any(), Mockito.any())).thenReturn(expectedMedia);
        Mockito.when(videoGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var input = UploadMediaInputCommand.with(expectedId.getValue(), expectedVideoResource);
        final var output = useCase.execute(input);

        Assertions.assertEquals(expectedType, output.mediaType());
        Assertions.assertEquals(expectedId.getValue(), output.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(mediaResourceGateway, Mockito.times(1)).storeAudioVideo(expectedId, expectedVideoResource);
        Mockito.verify(videoGateway, Mockito.times(1)).update(ArgumentMatchers.argThat(vid ->
                vid.getContent().isEmpty()
                && Objects.equals(expectedMedia, vid.getTrailer().get())
                && vid.getBanner().isEmpty()
                && vid.getThumbnail().isEmpty()
                && vid.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenCmdToUpload_whenIsValid_thenShouldUpdateBannerMediaAndPersistIt() {
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(video));
        Mockito.when(mediaResourceGateway.storeImage(Mockito.any(), Mockito.any())).thenReturn(expectedMedia);
        Mockito.when(videoGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var input = UploadMediaInputCommand.with(expectedId.getValue(), expectedVideoResource);
        final var output = useCase.execute(input);

        Assertions.assertEquals(expectedType, output.mediaType());
        Assertions.assertEquals(expectedId.getValue(), output.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(mediaResourceGateway, Mockito.times(1)).storeImage(expectedId, expectedVideoResource);
        Mockito.verify(videoGateway, Mockito.times(1)).update(ArgumentMatchers.argThat(vid ->
                vid.getContent().isEmpty()
                && vid.getTrailer().isEmpty()
                && Objects.equals(expectedMedia, vid.getBanner().get())
                && vid.getThumbnail().isEmpty()
                && vid.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenCmdToUpload_whenIsValid_thenShouldUpdateThumbnailMediaAndPersistIt() {
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(video));
        Mockito.when(mediaResourceGateway.storeImage(Mockito.any(), Mockito.any())).thenReturn(expectedMedia);
        Mockito.when(videoGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var input = UploadMediaInputCommand.with(expectedId.getValue(), expectedVideoResource);
        final var output = useCase.execute(input);

        Assertions.assertEquals(expectedType, output.mediaType());
        Assertions.assertEquals(expectedId.getValue(), output.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(mediaResourceGateway, Mockito.times(1)).storeImage(expectedId, expectedVideoResource);
        Mockito.verify(videoGateway, Mockito.times(1)).update(ArgumentMatchers.argThat(vid ->
                vid.getContent().isEmpty()
                && vid.getTrailer().isEmpty()
                && vid.getBanner().isEmpty()
                && Objects.equals(expectedMedia, vid.getThumbnail().get())
                && vid.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenCmdToUpload_whenIsValid_thenShouldUpdateThumbnailHalfMediaAndPersistIt() {
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(video));
        Mockito.when(mediaResourceGateway.storeImage(Mockito.any(), Mockito.any())).thenReturn(expectedMedia);
        Mockito.when(videoGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var input = UploadMediaInputCommand.with(expectedId.getValue(), expectedVideoResource);
        final var output = useCase.execute(input);

        Assertions.assertEquals(expectedType, output.mediaType());
        Assertions.assertEquals(expectedId.getValue(), output.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(mediaResourceGateway, Mockito.times(1)).storeImage(expectedId, expectedVideoResource);

        Mockito.verify(videoGateway, Mockito.times(1)).update(ArgumentMatchers.argThat(vid ->
                vid.getContent().isEmpty()
                && vid.getTrailer().isEmpty()
                && vid.getBanner().isEmpty()
                && vid.getThumbnail().isEmpty()
                && Objects.equals(expectedMedia, vid.getThumbnailHalf().get())
        ));
    }

    @Test
    void givenCmdToUpload_whenVideoIsInvalid_thenShouldReturnNotFound() {
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.empty());

        final var input = UploadMediaInputCommand.with(expectedId.getValue(), expectedVideoResource);
        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(input));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway, videoGateway);
    }

}

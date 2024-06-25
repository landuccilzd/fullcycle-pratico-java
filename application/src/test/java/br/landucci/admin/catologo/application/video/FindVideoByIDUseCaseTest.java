package br.landucci.admin.catologo.application.video;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.video.find.DefaultFindVideoByIDUseCase;
import br.landucci.admin.catologo.application.video.find.FindVideoByIDInputCommand;
import br.landucci.admin.catologo.application.Fixture;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class FindVideoByIDUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultFindVideoByIDUseCase useCase;

    @Mock
    private VideoGateway videoGateway;


    @Test
    void givenAValidId_whenFindingVideo_thenShouldReturnIt() {
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.peach().getId(),
                Fixture.CastMembers.zelda().getId()
        );
        final var expectedVideo = Fixture.Videos.audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var video = new VideoBuilder()
                .withId(VideoID.unique())
                .withTitle(expectedTitle)
                .withDescription(expectedDescription)
                .withLaunchedAt(expectedLaunchYear)
                .withDuration(expectedDuration)
                .withOpened(expectedOpened)
                .withPublished(expectedPublished)
                .withRating(expectedRating)
                .withCategories(expectedCategories)
                .withGenres(expectedGenres)
                .withCastMembers(expectedMembers)
                .build();

        video.updateContent(expectedVideo)
                .updateTrailer(expectedTrailer)
                .updateBanner(expectedBanner)
                .updateThumbnail(expectedThumb)
                .updateThumbnailHalf(expectedThumbHalf);

        final var expectedId = video.getId();

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));

        final var input = new FindVideoByIDInputCommand(expectedId.getValue());
        final var foundVideo = this.useCase.execute(input);

        Assertions.assertEquals(expectedId.getValue(), foundVideo.id());
        Assertions.assertEquals(expectedTitle, foundVideo.title());
        Assertions.assertEquals(expectedDescription, foundVideo.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), foundVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, foundVideo.duration());
        Assertions.assertEquals(expectedOpened, foundVideo.opened());
        Assertions.assertEquals(expectedPublished, foundVideo.published());
        Assertions.assertEquals(expectedRating, foundVideo.rating());
        Assertions.assertEquals(asString(expectedCategories), foundVideo.categories());
        Assertions.assertEquals(asString(expectedGenres), foundVideo.genres());
        Assertions.assertEquals(asString(expectedMembers), foundVideo.castMembers());
        Assertions.assertEquals(expectedVideo, foundVideo.video());
        Assertions.assertEquals(expectedTrailer, foundVideo.trailer());
        Assertions.assertEquals(expectedBanner, foundVideo.banner());
        Assertions.assertEquals(expectedThumb, foundVideo.thumbnail());
        Assertions.assertEquals(expectedThumbHalf, foundVideo.thumbnailHalf());
//        Assertions.assertEquals(video.getCreatedAt(), foundVideo.createdAt());
//        Assertions.assertEquals(video.getUpdatedAt(), foundVideo.updatedAt());
    }

    @Test
    void givenInvalidId_whenCallsGetVideo_shouldReturnNotFound() {
        final var expectedErrorMessage = "Video with ID 123 was not found";
        final var expectedId = VideoID.with("123");

        Mockito.when(videoGateway.findById(Mockito.any())).thenReturn(Optional.empty());

        final var input = new FindVideoByIDInputCommand(expectedId.getValue());
        final var actualError = Assertions.assertThrows(NotFoundException.class, () -> this.useCase.execute(input));

        Assertions.assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }
}

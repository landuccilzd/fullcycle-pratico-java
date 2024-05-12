package br.landucci.admin.catologo.application.video;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.video.update.DefaultUpdateVideoUseCase;
import br.landucci.admin.catologo.application.video.update.UpdateVideoInputCommand;
import br.landucci.admin.catologo.application.Fixture;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.exception.InternalErrorException;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;

import java.util.*;

class UpdateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateVideoUseCase useCase;

    @Mock
    private VideoGateway gateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Test
    void givenAValidCommand_whenUpdatingAVideo_thenShouldReturnVideoId() {
        final var video = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.zelda().getId(), Fixture.CastMembers.peach().getId());
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var input = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway).findById(video.getId());
//
//        Mockito.verify(gateway).update(ArgumentMatchers.argThat(vid ->
//                Objects.equals(expectedTitle, vid.getTitle())
//                && Objects.equals(expectedDescription, vid.getDescription())
//                && Objects.equals(expectedLaunchYear, vid.getLaunchedAt())
//                && Objects.equals(expectedDuration, vid.getDuration())
//                && Objects.equals(expectedOpened, vid.isOpened())
//                && Objects.equals(expectedPublished, vid.isPublished())
//                && Objects.equals(expectedRating, vid.getRating())
//                && Objects.equals(expectedCategories, vid.getCategories())
//                && Objects.equals(expectedGenres, vid.getGenres())
//                && Objects.equals(expectedMembers, vid.getCastMembers())
//                && Objects.equals(expectedVideo.getName(), vid.getContent().get().getName())
//                && Objects.equals(expectedTrailer.getName(), vid.getTrailer().get().getName())
//                && Objects.equals(expectedBanner.getName(), vid.getBanner().get().getName())
//                && Objects.equals(expectedThumb.getName(), vid.getThumbnail().get().getName())
//                && Objects.equals(expectedThumbHalf.getName(), vid.getThumbnailHalf().get().getName())
//                && Objects.equals(video.getCreatedAt(), vid.getCreatedAt())
//                && video.getUpdatedAt().isBefore(vid.getUpdatedAt())
//        ));
    }

    @Test
    void givenAValidCommandWithoutCategories_whenUpdatingVideo_thenShouldReturnVideoId() {
        final var video = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.peach().getId(), Fixture.CastMembers.zelda().getId());
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var input = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualResult = useCase.execute(input);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Mockito.verify(gateway).findById(video.getId());

//        Mockito.verify(gateway).update(ArgumentMatchers.argThat(vid ->
//                Objects.equals(expectedTitle, vid.getTitle())
//                && Objects.equals(expectedDescription, vid.getDescription())
//                && Objects.equals(expectedLaunchYear, vid.getLaunchedAt())
//                && Objects.equals(expectedDuration, vid.getDuration())
//                && Objects.equals(expectedOpened, vid.isOpened())
//                && Objects.equals(expectedPublished, vid.isPublished())
//                && Objects.equals(expectedRating, vid.getRating())
//                && Objects.equals(expectedCategories, vid.getCategories())
//                && Objects.equals(expectedGenres, vid.getGenres())
//                && Objects.equals(expectedMembers, vid.getCastMembers())
//                && Objects.equals(expectedVideo.getName(), vid.getContent().get().getName())
//                && Objects.equals(expectedTrailer.getName(), vid.getTrailer().get().getName())
//                && Objects.equals(expectedBanner.getName(), vid.getBanner().get().getName())
//                && Objects.equals(expectedThumb.getName(), vid.getThumbnail().get().getName())
//                && Objects.equals(expectedThumbHalf.getName(), vid.getThumbnailHalf().get().getName())
//                && Objects.equals(video.getCreatedAt(), vid.getCreatedAt())
//                && video.getUpdatedAt().isBefore(vid.getUpdatedAt())
//        ));
    }

    @Test
    void givenAValidCommandWithoutGenres_whenUpdatingVideo_thenShouldReturnVideoId() {
        final var video = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.of(Fixture.CastMembers.zelda().getId(), Fixture.CastMembers.peach().getId());
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var input = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualResult = useCase.execute(input);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Mockito.verify(gateway).findById(video.getId());

//        Mockito.verify(gateway).update(ArgumentMatchers.argThat(vid ->
//                Objects.equals(expectedTitle, vid.getTitle())
//                && Objects.equals(expectedDescription, vid.getDescription())
//                && Objects.equals(expectedLaunchYear, vid.getLaunchedAt())
//                && Objects.equals(expectedDuration, vid.getDuration())
//                && Objects.equals(expectedOpened, vid.isOpened())
//                && Objects.equals(expectedPublished, vid.isPublished())
//                && Objects.equals(expectedRating, vid.getRating())
//                && Objects.equals(expectedCategories, vid.getCategories())
//                && Objects.equals(expectedGenres, vid.getGenres())
//                && Objects.equals(expectedMembers, vid.getCastMembers())
//                && Objects.equals(expectedVideo.getName(), vid.getContent().get().getName())
//                && Objects.equals(expectedTrailer.getName(), vid.getTrailer().get().getName())
//                && Objects.equals(expectedBanner.getName(), vid.getBanner().get().getName())
//                && Objects.equals(expectedThumb.getName(), vid.getThumbnail().get().getName())
//                && Objects.equals(expectedThumbHalf.getName(), vid.getThumbnailHalf().get().getName())
//                && Objects.equals(video.getCreatedAt(), vid.getCreatedAt())
//                && video.getUpdatedAt().isBefore(vid.getUpdatedAt())
//        ));
    }

    @Test
    void givenAValidCommandWithoutCastMembers_whenUpdatingVideo_thenShouldReturnVideoId() {
        final var video = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.<CastMemberID>of();
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var input = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway).findById(video.getId());
//
//        Mockito.verify(gateway).update(ArgumentMatchers.argThat(vid ->
//                Objects.equals(expectedTitle, vid.getTitle())
//                && Objects.equals(expectedDescription, vid.getDescription())
//                && Objects.equals(expectedLaunchYear, vid.getLaunchedAt())
//                && Objects.equals(expectedDuration, vid.getDuration())
//                && Objects.equals(expectedOpened, vid.isOpened())
//                && Objects.equals(expectedPublished, vid.isPublished())
//                && Objects.equals(expectedRating, vid.getRating())
//                && Objects.equals(expectedCategories, vid.getCategories())
//                && Objects.equals(expectedGenres, vid.getGenres())
//                && Objects.equals(expectedMembers, vid.getCastMembers())
//                && Objects.equals(expectedVideo.getName(), vid.getContent().get().getName())
//                && Objects.equals(expectedTrailer.getName(), vid.getTrailer().get().getName())
//                && Objects.equals(expectedBanner.getName(), vid.getBanner().get().getName())
//                && Objects.equals(expectedThumb.getName(), vid.getThumbnail().get().getName())
//                && Objects.equals(expectedThumbHalf.getName(), vid.getThumbnailHalf().get().getName())
//                && Objects.equals(video.getCreatedAt(), vid.getCreatedAt())
//                && video.getUpdatedAt().isBefore(vid.getUpdatedAt())
//        ));
    }

    @Test
    void givenAValidCommandWithoutResources_whenUpdatingVideo_thenShouldReturnVideoId() {
        final var video = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.peach().getId(), Fixture.CastMembers.zelda().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var input = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));
        Mockito.when(gateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualResult = useCase.execute(input);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Mockito.verify(gateway).findById(video.getId());

//        Mockito.verify(gateway).update(ArgumentMatchers.argThat(vid ->
//                Objects.equals(expectedTitle, vid.getTitle())
//                && Objects.equals(expectedDescription, vid.getDescription())
//                && Objects.equals(expectedLaunchYear, vid.getLaunchedAt())
//                && Objects.equals(expectedDuration, vid.getDuration())
//                && Objects.equals(expectedOpened, vid.isOpened())
//                && Objects.equals(expectedPublished, vid.isPublished())
//                && Objects.equals(expectedRating, vid.getRating())
//                && Objects.equals(expectedCategories, vid.getCategories())
//                && Objects.equals(expectedGenres, vid.getGenres())
//                && Objects.equals(expectedMembers, vid.getCastMembers())
//                && vid.getContent().isEmpty()
//                && vid.getTrailer().isEmpty()
//                && vid.getBanner().isEmpty()
//                && vid.getThumbnail().isEmpty()
//                && vid.getThumbnailHalf().isEmpty()
//                && Objects.equals(video.getCreatedAt(), vid.getCreatedAt())
//                && video.getUpdatedAt().isBefore(vid.getUpdatedAt())
//        ));
    }

    @ParameterizedTest
    @CsvSource({
            ",1,Title should not be null",
            "empty,1,Title should not be empty",
            "ab,1,Title must have between 3 and 255 characters"
    })
    void givenAnInvalidTitle_whenUpdatingVideo_thenShouldReturnDomainException(final String expectedTitle,
            final int expectedErrorCount, final String expectedErrorMessage) {

        final var video = Fixture.Videos.video();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var title = "empty".equals(expectedTitle) ? "" : expectedTitle;
        final var input = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                title,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));

        final var actualException = Assertions.assertThrows(DomainException.class, () ->
            useCase.execute(input)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(gateway).findById(video.getId());
        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeVideo(Mockito.any(), Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @ParameterizedTest
    @CsvSource({
            ",1,Rating should not be null",
            "HGFDH,1,Rating should not be null"
    })
    void givenAnInvalidRating_whenUpdatingAVideo_thenShouldReturnDomainException(final String expectedRating,
            final int expectedErrorCount, final String expectedErrorMessage) {

        final var video = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var rating = "empty".equals(expectedRating) ? "" : expectedRating;
        final var command = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                rating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(gateway).findById(video.getId());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeVideo(Mockito.any(), Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenANullLaunchedAt_whenUpdatingVideo_thenShouldReturnDomainException() {
        final var video = Fixture.Videos.video();
        final var expectedErrorMessage = "Launched At should not be null";
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer expectedLaunchYear = null;
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var input = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));

        final var exception = Assertions.assertThrows(DomainException.class, () ->
            useCase.execute(input)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        Mockito.verify(gateway).findById(video.getId());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeVideo(Mockito.any(), Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAValidCommand_whenUpdatingAVideoAndSomeCategoriesDoesNotExists_thenShouldReturnDomainException() {
        final var video = Fixture.Videos.video();
        final var aulasId = Fixture.Categories.documentarios().getId();

        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(aulasId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulasId);
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.zelda().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var input = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>());
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        final var exception = Assertions.assertThrows(NotificationException.class, () ->
            useCase.execute(input)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);
        Mockito.verify(castMemberGateway, Mockito.times(1)).existsByIds(expectedMembers);
        Mockito.verify(genreGateway, Mockito.times(1)).existsByIds(expectedGenres);
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenAValidCommand_whenUpdatingAVideoAndSomeGenresDoesNotExists_thenShouldReturnDomainException() {
        final var video = Fixture.Videos.video();
        final var techId = Fixture.Genres.acao().getId();

        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(techId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.of(techId);
        final var expectedMembers = Set.of(Fixture.CastMembers.zelda().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var input = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>());

        final var exception = Assertions.assertThrows(NotificationException.class, () ->
            useCase.execute(input)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);
        Mockito.verify(castMemberGateway, Mockito.times(1)).existsByIds(expectedMembers);
        Mockito.verify(genreGateway, Mockito.times(1)).existsByIds(expectedGenres);
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAValidCommand_whenUpdatingVideoAndSomeCastMembersDoesNotExists_thenShouldReturnDomainException() {
        final var aVideo = Fixture.Videos.video();
        final var wesleyId = Fixture.CastMembers.peach().getId();

        final var expectedErrorMessage = "Some cast members could not be found: %s".formatted(wesleyId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.of(wesleyId);
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var input = UpdateVideoInputCommand.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(aVideo)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>());
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(input));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);
        Mockito.verify(castMemberGateway, Mockito.times(1)).existsByIds(expectedMembers);
        Mockito.verify(genreGateway, Mockito.times(1)).existsByIds(expectedGenres);
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAValidCommand_whenUpdatingVideoThrowsException_thenShouldCallClearResources() {
        final var video = Fixture.Videos.video();
        final var expectedErrorMessage = "An error on create video was observed [videoId:";
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.zelda().getId(), Fixture.CastMembers.peach().getId());
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var input = UpdateVideoInputCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(gateway.findById(Mockito.any())).thenReturn(Optional.of(Video.clone(video)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(gateway.update(Mockito.any())).thenThrow(new RuntimeException("Internal Server Error"));

        final var actualResult = Assertions.assertThrows(InternalErrorException.class, () -> useCase.execute(input));

        Assertions.assertNotNull(actualResult);
        Assertions.assertTrue(actualResult.getMessage().startsWith(expectedErrorMessage));

        Mockito.verify(mediaResourceGateway, Mockito.times(0)).clearResources(Mockito.any());
    }

    private void mockImageMedia() {
        Mockito.when(mediaResourceGateway.storeImage(Mockito.any(), Mockito.any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return ImageMedia.with(resource.getChecksum(), resource.getName(), "/img");
        });
    }

    private void mockAudioVideoMedia() {
        Mockito.when(mediaResourceGateway.storeVideo(Mockito.any(), Mockito.any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return VideoMedia.with(resource.getChecksum(), resource.getName(), "/img");
        });
    }

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway, categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway);
    }

}

package br.landucci.admin.catologo.application.video;

import br.landucci.admin.catologo.application.UseCaseTest;
import br.landucci.admin.catologo.application.video.create.CreateVideoInputCommand;
import br.landucci.admin.catologo.application.video.create.DefaultCreateVideoUseCase;
import br.landucci.admin.catologo.domain.Fixture;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway gateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Test
    void givenAValidCommand_whenCreatingAVideo_thenShouldReturnVideoId() {
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.jehnifferAniston().getId(), Fixture.CastMembers.meganFox().getId());
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var input = CreateVideoInputCommand.with(expectedTitle, expectedDescription,
                expectedLaunchYear.getValue(), expectedDuration, expectedOpened, expectedPublished,
                expectedRating.getName(), asString(expectedCategories), asString(expectedGenres),
                asString(expectedMembers), expectedVideo, expectedTrailer, expectedBanner, expectedThumb,
                expectedThumbHalf);

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway).create(ArgumentMatchers.argThat(video ->
                Objects.equals(expectedTitle, video.getTitle())
                && Objects.equals(expectedDescription, video.getDescription())
                && Objects.equals(expectedLaunchYear, video.getLaunchedAt())
                && Objects.equals(expectedDuration, video.getDuration())
                && Objects.equals(expectedOpened, video.isOpened())
                && Objects.equals(expectedPublished, video.isPublished())
                && Objects.equals(expectedRating, video.getRating())
                && Objects.equals(expectedCategories, video.getCategories())
                && Objects.equals(expectedGenres, video.getGenres())
                && Objects.equals(expectedMembers, video.getCastMembers())
                && Objects.equals(expectedVideo.getName(), video.getContent().get().getName())
                && Objects.equals(expectedTrailer.getName(), video.getTrailer().get().getName())
                && Objects.equals(expectedBanner.getName(), video.getBanner().get().getName())
                && Objects.equals(expectedThumb.getName(), video.getThumbnail().get().getName())
                && Objects.equals(expectedThumbHalf.getName(), video.getThumbnailHalf().get().getName())
        ));
    }

    @Test
    void givenAValidCommandWithoutCategories_whenCallsCreateVideo_shouldReturnVideoId() {
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.meganFox().getId());
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var input = CreateVideoInputCommand.with(expectedTitle, expectedDescription,
                expectedLaunchYear.getValue(), expectedDuration, expectedOpened, expectedPublished,
                expectedRating.getName(), asString(expectedCategories), asString(expectedGenres),
                asString(expectedMembers), expectedVideo, expectedTrailer, expectedBanner, expectedThumb,
                expectedThumbHalf);

        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway).create(ArgumentMatchers.argThat(video ->
                Objects.equals(expectedTitle, video.getTitle())
                && Objects.equals(expectedDescription, video.getDescription())
                && Objects.equals(expectedLaunchYear, video.getLaunchedAt())
                && Objects.equals(expectedDuration, video.getDuration())
                && Objects.equals(expectedOpened, video.isOpened())
                && Objects.equals(expectedPublished, video.isPublished())
                && Objects.equals(expectedRating, video.getRating())
                && Objects.equals(expectedCategories, video.getCategories())
                && Objects.equals(expectedGenres, video.getGenres())
                && Objects.equals(expectedMembers, video.getCastMembers())
                && Objects.equals(expectedVideo.getName(), video.getContent().get().getName())
                && Objects.equals(expectedTrailer.getName(), video.getTrailer().get().getName())
                && Objects.equals(expectedBanner.getName(), video.getBanner().get().getName())
                && Objects.equals(expectedThumb.getName(), video.getThumbnail().get().getName())
                && Objects.equals(expectedThumbHalf.getName(), video.getThumbnailHalf().get().getName())
        ));
    }

    @Test
    void givenAValidCommandWithoutGenres_whenCreatingAVideo_thenShouldReturnVideoId() {
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.of(
                Fixture.CastMembers.meganFox().getId(),
                Fixture.CastMembers.jehnifferAniston().getId()
        );
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var input = CreateVideoInputCommand.with(
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

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway).create(ArgumentMatchers.argThat(video ->
                Objects.equals(expectedTitle, video.getTitle())
                && Objects.equals(expectedDescription, video.getDescription())
                && Objects.equals(expectedLaunchYear, video.getLaunchedAt())
                && Objects.equals(expectedDuration, video.getDuration())
                && Objects.equals(expectedOpened, video.isOpened())
                && Objects.equals(expectedPublished, video.isPublished())
                && Objects.equals(expectedRating, video.getRating())
                && Objects.equals(expectedCategories, video.getCategories())
                && Objects.equals(expectedGenres, video.getGenres())
                && Objects.equals(expectedMembers, video.getCastMembers())
                && Objects.equals(expectedVideo.getName(), video.getContent().get().getName())
                && Objects.equals(expectedTrailer.getName(), video.getTrailer().get().getName())
                && Objects.equals(expectedBanner.getName(), video.getBanner().get().getName())
                && Objects.equals(expectedThumb.getName(), video.getThumbnail().get().getName())
                && Objects.equals(expectedThumbHalf.getName(), video.getThumbnailHalf().get().getName())
        ));
    }

    @Test
    void givenAValidCommandWithoutCastMembers_whenCreatingAVideo_thensShouldReturnVideoId() {
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

        final var input = CreateVideoInputCommand.with(
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

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualResult = useCase.execute(input);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Mockito.verify(gateway).create(ArgumentMatchers.argThat(video ->
                Objects.equals(expectedTitle, video.getTitle())
                && Objects.equals(expectedDescription, video.getDescription())
                && Objects.equals(expectedLaunchYear, video.getLaunchedAt())
                && Objects.equals(expectedDuration, video.getDuration())
                && Objects.equals(expectedOpened, video.isOpened())
                && Objects.equals(expectedPublished, video.isPublished())
                && Objects.equals(expectedRating, video.getRating())
                && Objects.equals(expectedCategories, video.getCategories())
                && Objects.equals(expectedGenres, video.getGenres())
                && Objects.equals(expectedMembers, video.getCastMembers())
                && Objects.equals(expectedVideo.getName(), video.getContent().get().getName())
                && Objects.equals(expectedTrailer.getName(), video.getTrailer().get().getName())
                && Objects.equals(expectedBanner.getName(), video.getBanner().get().getName())
                && Objects.equals(expectedThumb.getName(), video.getThumbnail().get().getName())
                && Objects.equals(expectedThumbHalf.getName(), video.getThumbnailHalf().get().getName())
        ));
    }

    @Test
    void givenAValidCommandWithoutResources_whenCreatingAVideo_shouldReturnVideoId() {
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.filmes().getId());
        final var expectedGenres = Set.of(Fixture.Genres.acao().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.jehnifferAniston().getId(), Fixture.CastMembers.meganFox().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var input = CreateVideoInputCommand.with(
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

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));
        Mockito.when(gateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(input);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway).create(ArgumentMatchers.argThat(video ->
                Objects.equals(expectedTitle, video.getTitle())
                && Objects.equals(expectedDescription, video.getDescription())
                && Objects.equals(expectedLaunchYear, video.getLaunchedAt())
                && Objects.equals(expectedDuration, video.getDuration())
                && Objects.equals(expectedOpened, video.isOpened())
                && Objects.equals(expectedPublished, video.isPublished())
                && Objects.equals(expectedRating, video.getRating())
                && Objects.equals(expectedCategories, video.getCategories())
                && Objects.equals(expectedGenres, video.getGenres())
                && Objects.equals(expectedMembers, video.getCastMembers())
                && video.getContent().isEmpty()
                && video.getTrailer().isEmpty()
                && video.getBanner().isEmpty()
                && video.getThumbnail().isEmpty()
                && video.getThumbnailHalf().isEmpty()
        ));
    }

    @ParameterizedTest
    @CsvSource({
            ",1,Title should not be null",
            "empty,1,Title should not be empty",
            "ab,1,Title must have between 3 and 255 characters"
    })
    void givenANullTitle_whenCallsCreateVideo_shouldReturnDomainException(final String expectedTitle,
            final int expectedErrorCount, final String expectedErrorMessage) {

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
        final var input = CreateVideoInputCommand.with(
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

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(input));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeAudioVideo(Mockito.any(), Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }


    @ParameterizedTest
    @CsvSource({
            ",1,Rating should not be null",
            "JAIAJIA,1,Rating should not be null"
    })
    void givenAnInvalidRating_whenCreatingVideo_thenShouldReturnDomainException(final String expectedRating,
            final int expectedErrorCount, final String expectedErrorMessage) {

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

        final var input = CreateVideoInputCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        final var exception = Assertions.assertThrows(NotificationException.class, () ->
            useCase.execute(input)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenANullLaunchYear_whenCreatingAVideo_thenShouldReturnDomainException() {
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

        final var input = CreateVideoInputCommand.with(
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

        final var exception = Assertions.assertThrows(NotificationException.class, () ->
            useCase.execute(input)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAValidCommand_whenCreatingAVideoAndSomeCategoriesDoesNotExists_thenShouldReturnDomainException() {
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
        final var expectedMembers = Set.of(Fixture.CastMembers.meganFox().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoInputCommand.with(
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

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>());
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        final var actualException = Assertions.assertThrows(NotificationException.class, () ->
            useCase.execute(aCommand)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);
        Mockito.verify(castMemberGateway, Mockito.times(1)).existsByIds(expectedMembers);
        Mockito.verify(genreGateway, Mockito.times(1)).existsByIds(expectedGenres);
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAValidCommand_whenCreatingAVideoAndSomeGenresDoesNotExists_thenShouldReturnDomainException() {
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
        final var expectedMembers = Set.of(Fixture.CastMembers.meganFox().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var input = CreateVideoInputCommand.with(
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

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(input);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);
        Mockito.verify(castMemberGateway, Mockito.times(1)).existsByIds(expectedMembers);
        Mockito.verify(genreGateway, Mockito.times(1)).existsByIds(expectedGenres);
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAValidCommand_whenCreatingVideoAndSomeCastMembersDoesNotExists_thenShouldReturnDomainException() {
        final var wesleyId = Fixture.CastMembers.jehnifferAniston().getId();

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

        final var input = CreateVideoInputCommand.with(
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

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>());
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        final var actualException = Assertions.assertThrows(NotificationException.class, () ->
            useCase.execute(input)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);
        Mockito.verify(castMemberGateway, Mockito.times(1)).existsByIds(expectedMembers);
        Mockito.verify(genreGateway, Mockito.times(1)).existsByIds(expectedGenres);
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenAValidCommand_whenCreatingAVideoThrowsException_thenShouldCallClearResources() {
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
        final var expectedMembers = Set.of(Fixture.CastMembers.meganFox().getId(), Fixture.CastMembers.jehnifferAniston().getId());
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var input = CreateVideoInputCommand.with(
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

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(gateway.create(Mockito.any())).thenThrow(new RuntimeException("Internal Server Error"));

        final var exception = Assertions.assertThrows(InternalErrorException.class, () ->
            useCase.execute(input)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception.getMessage().startsWith(expectedErrorMessage));

        Mockito.verify(mediaResourceGateway).clearResources(Mockito.any());
    }

    private void mockImageMedia() {
        Mockito.when(mediaResourceGateway.storeImage(Mockito.any(), Mockito.any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return ImageMedia.with(resource.getChecksum(), resource.getName(), "/img");
        });
    }

    private void mockAudioVideoMedia() {
        Mockito.when(mediaResourceGateway.storeAudioVideo(Mockito.any(), Mockito.any())).thenAnswer(t -> {
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
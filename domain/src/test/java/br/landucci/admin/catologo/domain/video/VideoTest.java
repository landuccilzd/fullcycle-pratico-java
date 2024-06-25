package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.UnitTest;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.utils.InstantUtils;
import br.landucci.admin.catologo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

class VideoTest extends UnitTest {

    private static final String[] EXPECTED_TITLES = {
            "O Jogo da imitação",
            "Truque de Mestre"
    };
    private static final String[] EXPECTED_DESCRIPTIONS = {
            """
                Durante a segunda Guerra Mundial, um matemático lidera uma equipe de analistas de criptografia pra
                decifrar o famoso código alemão Enigma.
            """,
            """
                Um grupo de ilusionistas se especializa em roubar bancos e dar o dinheiro para o público enquanto
                despista agentes do FBI
            """
    };
    private static final Year[] EXPECTED_LAUNCHED_AT = {
            Year.of(2014),
            Year.of(2013)
    };
    private static final double[] EXPECTED_DURATIONS = {115.0, 114.0};
    private static final boolean[] EXPECTED_OPENED = {false, true};
    private static final boolean[] EXPECTED_PUBLISHED = {true, false};
    private static final Rating[] EXPECTED_RATINGS = {Rating.AGE_12, Rating.AGE_12};


    @Test
    void givenValidParams_whenCreatingANewVideo_thenShouldInstantiateIt() {
        final var expectedTitle = "O Enigma dos Números";
        final var expectedDescription = """
                    Inpirada pela mãe em enquanto jogava um jogo de celular com a prima, uma estudante de mestrado que 
                    trabalha de home office tem que decifrar O ENIGMA DOS NÚMEROS para poder concluir o seu mestrado e voltar
                    a ter vida
                """;
        final var expectedLauchedAt = Year.of(2024);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var expectedCategories = Set.of(filmes.getId());

        final var terror = Genre.newGenre("Terror", true);
        final var ficcao = Genre.newGenre("Ficção Científica", true);
        final var expectedGenres = Set.of(terror.getId(), ficcao.getId());

        final var cogu = CastMember.newCastMember("Malu", CastMemberType.DIRECTOR);
        final var biju = CastMember.newCastMember("Julia", CastMemberType.ACTOR);
        final var mandy = CastMember.newCastMember("Ammanda", CastMemberType.ACTOR);
        final var expectedMembers = Set.of(cogu.getId(), biju.getId(), mandy.getId());

        final var video = createVideo(expectedTitle, expectedDescription, expectedLauchedAt, expectedDuration,
                expectedOpened, expectedPublished, expectedRating, expectedCategories, expectedGenres, expectedMembers);

        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertNotNull(video.getCreatedAt());
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertEquals(video.getCreatedAt(), video.getUpdatedAt());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLauchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPublished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertEquals(expectedCategories, video.getCategories());
        Assertions.assertEquals(expectedGenres, video.getGenres());
        Assertions.assertEquals(expectedMembers, video.getCastMembers());
        Assertions.assertTrue(video.getContent().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertTrue(video.getBanner().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbnailHalf().isEmpty());
        Assertions.assertTrue(video.getDomainEvents().isEmpty());

        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidParams_whenCreatingANewVideo_thenShouldInstantiate() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var video = createVideo(
                EXPECTED_TITLES[0],
                EXPECTED_DESCRIPTIONS[0],
                EXPECTED_LAUNCHED_AT[0],
                EXPECTED_DURATIONS[0],
                EXPECTED_OPENED[0],
                EXPECTED_PUBLISHED[0],
                EXPECTED_RATINGS[0],
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertNotNull(video.getCreatedAt());
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertEquals(video.getCreatedAt(), video.getUpdatedAt());
        Assertions.assertEquals(EXPECTED_TITLES[0], video.getTitle());
        Assertions.assertEquals(EXPECTED_DESCRIPTIONS[0], video.getDescription());
        Assertions.assertEquals(EXPECTED_LAUNCHED_AT[0], video.getLaunchedAt());
        Assertions.assertEquals(EXPECTED_DURATIONS[0], video.getDuration());
        Assertions.assertEquals(EXPECTED_OPENED[0], video.isOpened());
        Assertions.assertEquals(EXPECTED_PUBLISHED[0], video.isPublished());
        Assertions.assertEquals(EXPECTED_RATINGS[0], video.getRating());
        Assertions.assertEquals(expectedCategories, video.getCategories());
        Assertions.assertEquals(expectedGenres, video.getGenres());
        Assertions.assertEquals(expectedMembers, video.getCastMembers());
        Assertions.assertTrue(video.getContent().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertTrue(video.getBanner().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbnailHalf().isEmpty());
        Assertions.assertTrue(video.getDomainEvents().isEmpty());

        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenUpdating_thenShouldReturnUpdated() {
        final var expectedEventCount = 1;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedEvent = new VideoMediaCreated("ID", "file");

        final var video = createVideo(
                EXPECTED_TITLES[0],
                EXPECTED_DESCRIPTIONS[0],
                EXPECTED_LAUNCHED_AT[0],
                EXPECTED_DURATIONS[0],
                EXPECTED_OPENED[0],
                EXPECTED_PUBLISHED[0],
                EXPECTED_RATINGS[0],
                Set.of(),
                Set.of(),
                Set.of()
        );

        video.registerEvent(expectedEvent);

        final var updatedVideo = Video.clone(video);
        updatedVideo.updateTitle(EXPECTED_TITLES[1])
                .updateDescription(EXPECTED_DESCRIPTIONS[1])
                .updateLaunchedAt(EXPECTED_LAUNCHED_AT[1])
                .updateDuration(EXPECTED_DURATIONS[1])
                .open()
                .unpublish()
                .updateRating(EXPECTED_RATINGS[1])
                .updateCategories(expectedCategories)
                .updateGenres(expectedGenres)
                .updateCastMembers(expectedMembers);

        Assertions.assertNotNull(updatedVideo);
        Assertions.assertNotNull(updatedVideo.getId());
//        Assertions.assertEquals(video.getCreatedAt(), updatedVideo.getCreatedAt());
//        Assertions.assertTrue(video.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        Assertions.assertEquals(EXPECTED_TITLES[1], updatedVideo.getTitle());
        Assertions.assertEquals(EXPECTED_DESCRIPTIONS[1], updatedVideo.getDescription());
        Assertions.assertEquals(EXPECTED_LAUNCHED_AT[1], updatedVideo.getLaunchedAt());
        Assertions.assertEquals(EXPECTED_DURATIONS[1], updatedVideo.getDuration());
        Assertions.assertEquals(EXPECTED_OPENED[1], updatedVideo.isOpened());
        Assertions.assertEquals(EXPECTED_PUBLISHED[1], updatedVideo.isPublished());
        Assertions.assertEquals(EXPECTED_RATINGS[1], updatedVideo.getRating());
        Assertions.assertEquals(expectedCategories, updatedVideo.getCategories());
        Assertions.assertEquals(expectedGenres, updatedVideo.getGenres());
        Assertions.assertEquals(expectedMembers, updatedVideo.getCastMembers());
        Assertions.assertTrue(updatedVideo.getContent().isEmpty());
        Assertions.assertTrue(updatedVideo.getTrailer().isEmpty());
        Assertions.assertTrue(updatedVideo.getBanner().isEmpty());
        Assertions.assertTrue(updatedVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        Assertions.assertEquals(expectedEventCount, updatedVideo.getDomainEvents().size());
        Assertions.assertEquals(expectedEvent, updatedVideo.getDomainEvents().get(0));

        Assertions.assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenUpdatingVideoMedia_thenShouldReturnUpdated() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedDomainEventSize = 1;
        final var content = VideoMedia.with("abc", "Video.mp4", "/123/videos");

        final var video = createVideo(
                EXPECTED_TITLES[0],
                EXPECTED_DESCRIPTIONS[0],
                EXPECTED_LAUNCHED_AT[0],
                EXPECTED_DURATIONS[0],
                EXPECTED_OPENED[0],
                EXPECTED_PUBLISHED[0],
                EXPECTED_RATINGS[0],
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var updatedVideo = Video.clone(video);
        updatedVideo.updateContent(content);

        Assertions.assertNotNull(updatedVideo);
        Assertions.assertNotNull(updatedVideo.getId());
//        Assertions.assertEquals(video.getCreatedAt(), updatedVideo.getCreatedAt());
//        Assertions.assertTrue(video.getUpdatedAt().isBefore(updateVideo.getUpdatedAt()));
        Assertions.assertEquals(EXPECTED_TITLES[0], updatedVideo.getTitle());
        Assertions.assertEquals(EXPECTED_DESCRIPTIONS[0], updatedVideo.getDescription());
        Assertions.assertEquals(EXPECTED_LAUNCHED_AT[0], updatedVideo.getLaunchedAt());
        Assertions.assertEquals(EXPECTED_DURATIONS[0], updatedVideo.getDuration());
        Assertions.assertEquals(EXPECTED_OPENED[0], updatedVideo.isOpened());
        Assertions.assertEquals(EXPECTED_PUBLISHED[0], updatedVideo.isPublished());
        Assertions.assertEquals(EXPECTED_RATINGS[0], updatedVideo.getRating());
        Assertions.assertEquals(expectedCategories, updatedVideo.getCategories());
        Assertions.assertEquals(expectedGenres, updatedVideo.getGenres());
        Assertions.assertEquals(expectedMembers, updatedVideo.getCastMembers());
        Assertions.assertEquals(content, updatedVideo.getContent().get());
        Assertions.assertTrue(updatedVideo.getTrailer().isEmpty());
        Assertions.assertTrue(updatedVideo.getBanner().isEmpty());
        Assertions.assertTrue(updatedVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        Assertions.assertEquals(expectedDomainEventSize, updatedVideo.getDomainEvents().size());

        final var event = (VideoMediaCreated) updatedVideo.getDomainEvents().get(0);
        Assertions.assertEquals(video.getId().getValue(), event.resourceId());
        Assertions.assertEquals(content.getRawLocation(), event.filePath());
        Assertions.assertNotNull(event.occurredOn());

        Assertions.assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenUpdatingTrailerMedia_thenShouldReturnUpdated() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedDomainEventSize = 1;
        final var trailer = VideoMedia.with("abc", "Trailer.mp4", "/123/videos");

        final var video = createVideo(
                EXPECTED_TITLES[0],
                EXPECTED_DESCRIPTIONS[0],
                EXPECTED_LAUNCHED_AT[0],
                EXPECTED_DURATIONS[0],
                EXPECTED_OPENED[0],
                EXPECTED_PUBLISHED[0],
                EXPECTED_RATINGS[0],
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var updatedVideo = Video.clone(video);
        updatedVideo.updateTrailer(trailer);

        Assertions.assertNotNull(updatedVideo);
        Assertions.assertNotNull(updatedVideo.getId());
//        Assertions.assertEquals(video.getCreatedAt(), updatedVideo.getCreatedAt());
//        Assertions.assertTrue(video.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        Assertions.assertEquals(EXPECTED_TITLES[0], updatedVideo.getTitle());
        Assertions.assertEquals(EXPECTED_DESCRIPTIONS[0], updatedVideo.getDescription());
        Assertions.assertEquals(EXPECTED_LAUNCHED_AT[0], updatedVideo.getLaunchedAt());
        Assertions.assertEquals(EXPECTED_DURATIONS[0], updatedVideo.getDuration());
        Assertions.assertEquals(EXPECTED_OPENED[0], updatedVideo.isOpened());
        Assertions.assertEquals(EXPECTED_PUBLISHED[0], updatedVideo.isPublished());
        Assertions.assertEquals(EXPECTED_RATINGS[0], updatedVideo.getRating());
        Assertions.assertEquals(expectedCategories, updatedVideo.getCategories());
        Assertions.assertEquals(expectedGenres, updatedVideo.getGenres());
        Assertions.assertEquals(expectedMembers, updatedVideo.getCastMembers());
        Assertions.assertTrue(updatedVideo.getContent().isEmpty());
        Assertions.assertEquals(trailer, updatedVideo.getTrailer().get());
        Assertions.assertTrue(updatedVideo.getBanner().isEmpty());
        Assertions.assertTrue(updatedVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        Assertions.assertEquals(expectedDomainEventSize, updatedVideo.getDomainEvents().size());

        final var event = (VideoMediaCreated) updatedVideo.getDomainEvents().get(0);
        Assertions.assertEquals(video.getId().getValue(), event.resourceId());
        Assertions.assertEquals(trailer.getRawLocation(), event.filePath());
        Assertions.assertNotNull(event.occurredOn());

        Assertions.assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenUpdatingBannerMedia_thenShouldReturnUpdated() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var banner = ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

        final var video = createVideo(
                EXPECTED_TITLES[0],
                EXPECTED_DESCRIPTIONS[0],
                EXPECTED_LAUNCHED_AT[0],
                EXPECTED_DURATIONS[0],
                EXPECTED_OPENED[0],
                EXPECTED_PUBLISHED[0],
                EXPECTED_RATINGS[0],
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var updatedBunnerMedia = Video.clone(video);
        updatedBunnerMedia.updateBanner(banner);

        Assertions.assertNotNull(updatedBunnerMedia);
        Assertions.assertNotNull(updatedBunnerMedia.getId());
//        Assertions.assertEquals(video.getCreatedAt(), updatedBunnerMedia.getCreatedAt());
//        Assertions.assertTrue(video.getUpdatedAt().isBefore(updatedBunnerMedia.getUpdatedAt()));
        Assertions.assertEquals(EXPECTED_TITLES[0], updatedBunnerMedia.getTitle());
        Assertions.assertEquals(EXPECTED_DESCRIPTIONS[0], updatedBunnerMedia.getDescription());
        Assertions.assertEquals(EXPECTED_LAUNCHED_AT[0], updatedBunnerMedia.getLaunchedAt());
        Assertions.assertEquals(EXPECTED_DURATIONS[0], updatedBunnerMedia.getDuration());
        Assertions.assertEquals(EXPECTED_OPENED[0], updatedBunnerMedia.isOpened());
        Assertions.assertEquals(EXPECTED_PUBLISHED[0], updatedBunnerMedia.isPublished());
        Assertions.assertEquals(EXPECTED_RATINGS[0], updatedBunnerMedia.getRating());
        Assertions.assertEquals(expectedCategories, updatedBunnerMedia.getCategories());
        Assertions.assertEquals(expectedGenres, updatedBunnerMedia.getGenres());
        Assertions.assertEquals(expectedMembers, updatedBunnerMedia.getCastMembers());
        Assertions.assertTrue(updatedBunnerMedia.getContent().isEmpty());
        Assertions.assertTrue(updatedBunnerMedia.getTrailer().isEmpty());
        Assertions.assertEquals(banner, updatedBunnerMedia.getBanner().get());
        Assertions.assertTrue(updatedBunnerMedia.getThumbnail().isEmpty());
        Assertions.assertTrue(updatedBunnerMedia.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> updatedBunnerMedia.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenUpdatingThumbnailMedia_thenShouldReturnUpdated() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var thumbnail = ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

        final var video = createVideo(
                EXPECTED_TITLES[0],
                EXPECTED_DESCRIPTIONS[0],
                EXPECTED_LAUNCHED_AT[0],
                EXPECTED_DURATIONS[0],
                EXPECTED_OPENED[0],
                EXPECTED_PUBLISHED[0],
                EXPECTED_RATINGS[0],
                expectedCategories,
                expectedGenres,
                expectedMembers
        );


        final var updatedVideo = Video.clone(video);
        updatedVideo.updateThumbnail(thumbnail);

        Assertions.assertNotNull(updatedVideo);
        Assertions.assertNotNull(updatedVideo.getId());
//        Assertions.assertEquals(video.getCreatedAt(), updatedVideo.getCreatedAt());
//        Assertions.assertTrue(video.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        Assertions.assertEquals(EXPECTED_TITLES[0], updatedVideo.getTitle());
        Assertions.assertEquals(EXPECTED_DESCRIPTIONS[0], updatedVideo.getDescription());
        Assertions.assertEquals(EXPECTED_LAUNCHED_AT[0], updatedVideo.getLaunchedAt());
        Assertions.assertEquals(EXPECTED_DURATIONS[0], updatedVideo.getDuration());
        Assertions.assertEquals(EXPECTED_OPENED[0], updatedVideo.isOpened());
        Assertions.assertEquals(EXPECTED_PUBLISHED[0], updatedVideo.isPublished());
        Assertions.assertEquals(EXPECTED_RATINGS[0], updatedVideo.getRating());
        Assertions.assertEquals(expectedCategories, updatedVideo.getCategories());
        Assertions.assertEquals(expectedGenres, updatedVideo.getGenres());
        Assertions.assertEquals(expectedMembers, updatedVideo.getCastMembers());
        Assertions.assertTrue(updatedVideo.getContent().isEmpty());
        Assertions.assertTrue(updatedVideo.getTrailer().isEmpty());
        Assertions.assertTrue(updatedVideo.getBanner().isEmpty());
        Assertions.assertEquals(thumbnail, updatedVideo.getThumbnail().get());
        Assertions.assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenUpdatingThumbnailHalfMedia_thenShouldReturnUpdated() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var thumbnail = ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

        final var video = createVideo(
                EXPECTED_TITLES[0],
                EXPECTED_DESCRIPTIONS[0],
                EXPECTED_LAUNCHED_AT[0],
                EXPECTED_DURATIONS[0],
                EXPECTED_OPENED[0],
                EXPECTED_PUBLISHED[0],
                EXPECTED_RATINGS[0],
                expectedCategories,
                expectedGenres,
                expectedMembers
        );


        final var updatedVideo = Video.clone(video);
        updatedVideo.updateThumbnailHalf(thumbnail);

        Assertions.assertNotNull(updatedVideo);
        Assertions.assertNotNull(updatedVideo.getId());
//        Assertions.assertEquals(video.getCreatedAt(), updatedVideo.getCreatedAt());
//        Assertions.assertTrue(video.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        Assertions.assertEquals(EXPECTED_TITLES[0], updatedVideo.getTitle());
        Assertions.assertEquals(EXPECTED_DESCRIPTIONS[0], updatedVideo.getDescription());
        Assertions.assertEquals(EXPECTED_LAUNCHED_AT[0], updatedVideo.getLaunchedAt());
        Assertions.assertEquals(EXPECTED_DURATIONS[0], updatedVideo.getDuration());
        Assertions.assertEquals(EXPECTED_OPENED[0], updatedVideo.isOpened());
        Assertions.assertEquals(EXPECTED_PUBLISHED[0], updatedVideo.isPublished());
        Assertions.assertEquals(EXPECTED_RATINGS[0], updatedVideo.getRating());
        Assertions.assertEquals(expectedCategories, updatedVideo.getCategories());
        Assertions.assertEquals(expectedGenres, updatedVideo.getGenres());
        Assertions.assertEquals(expectedMembers, updatedVideo.getCastMembers());
        Assertions.assertTrue(updatedVideo.getContent().isEmpty());
        Assertions.assertTrue(updatedVideo.getTrailer().isEmpty());
        Assertions.assertTrue(updatedVideo.getBanner().isEmpty());
        Assertions.assertTrue(updatedVideo.getThumbnail().isEmpty());
        Assertions.assertEquals(thumbnail, updatedVideo.getThumbnailHalf().get());

        Assertions.assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsWith_shouldCreateWithoutEvents() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var video = createVideo(
                EXPECTED_TITLES[0],
                EXPECTED_DESCRIPTIONS[0],
                EXPECTED_LAUNCHED_AT[0],
                EXPECTED_DURATIONS[0],
                EXPECTED_OPENED[0],
                EXPECTED_PUBLISHED[0],
                EXPECTED_RATINGS[0],
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        Assertions.assertNotNull(video.getDomainEvents());
    }

    private Video createVideo(final String title, final String description, final Year launchedAt,
                              final double duration, final boolean opened, final boolean published, final Rating rating,
                              final Set<CategoryID> categories, final Set<GenreID> genres, final Set<CastMemberID> castMembers) {
        return new VideoBuilder()
                .withId(VideoID.unique())
                .withTitle(title)
                .withDescription(description)
                .withLaunchedAt(launchedAt)
                .withDuration(duration)
                .withOpened(opened)
                .withPublished(published)
                .withRating(rating)
                .withCreatedAt(InstantUtils.now())
                .withUpdatedAt(InstantUtils.now())
                .withBanner(null)
                .withThumbnail(null)
                .withThumbnailHalf(null)
                .withTrailer(null)
                .withContent(null)
                .withCategories(categories)
                .withGenres(genres)
                .withCastMembers(castMembers)
                .withDomainEvents(null)
                .build();
    }
}
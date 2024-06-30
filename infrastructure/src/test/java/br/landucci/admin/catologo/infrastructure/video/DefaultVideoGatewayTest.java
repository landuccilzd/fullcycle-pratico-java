package br.landucci.admin.catologo.infrastructure.video;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.video.*;
import br.landucci.admin.catologo.infrastructure.Fixture;
import br.landucci.admin.catologo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.Year;
import java.util.Set;

@IntegrationTest
class DefaultVideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private VideoRepository videoRepository;

    private CastMember zelda;
    private CastMember peach;

    private Category filmes;
    private Category series;

    private Genre acao;
    private Genre comedia;

    @BeforeEach
    public void setUp() {
        zelda = castMemberGateway.create(Fixture.CastMembers.zelda());
        peach = castMemberGateway.create(Fixture.CastMembers.peach());
        filmes = categoryGateway.create(Fixture.Categories.filmes());
        series = categoryGateway.create(Fixture.Categories.series());
        acao = genreGateway.create(Fixture.Genres.acao());
        comedia = genreGateway.create(Fixture.Genres.comedia());
    }

    @Test
    @Transactional
    void givenAValidVideo_whenCreating_thenShouldPersistIt() {
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(filmes.getId());
        final var expectedGenres = Set.of(acao.getId());
        final var expectedMembers = Set.of(zelda.getId());
//        final var expectedVideo = VideoMedia.with("123", "video", "/media/video");
//        final var expectedTrailer = VideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

        final var video = new VideoBuilder()
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
                .newVideo()
//                .updateContent(expectedVideo)
//                .updateTrailer(expectedTrailer)
                .updateBanner(expectedBanner)
                .updateThumbnail(expectedThumb)
                .updateThumbnailHalf(expectedThumbHalf);

        final var createdVideo = videoGateway.create(video);

        Assertions.assertNotNull(createdVideo);
        Assertions.assertNotNull(createdVideo.getId());

        Assertions.assertEquals(expectedTitle, createdVideo.getTitle());
        Assertions.assertEquals(expectedDescription, createdVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, createdVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, createdVideo.getDuration());
        Assertions.assertEquals(expectedOpened, createdVideo.isOpened());
        Assertions.assertEquals(expectedPublished, createdVideo.isPublished());
        Assertions.assertEquals(expectedRating, createdVideo.getRating());
        Assertions.assertEquals(expectedCategories, createdVideo.getCategories());
        Assertions.assertEquals(expectedGenres, createdVideo.getGenres());
        Assertions.assertEquals(expectedMembers, createdVideo.getCastMembers());
//        Assertions.assertEquals(expectedVideo.getName(), createdVideo.getContent().get().getName());
//        Assertions.assertEquals(expectedTrailer.getName(), createdVideo.getTrailer().get().getName());
        Assertions.assertEquals(expectedBanner.getName(), createdVideo.getBanner().get().getName());
        Assertions.assertEquals(expectedThumb.getName(), createdVideo.getThumbnail().get().getName());
        Assertions.assertEquals(expectedThumbHalf.getName(), createdVideo.getThumbnailHalf().get().getName());

        final var persistedVideo = videoRepository.findById(createdVideo.getId().getValue()).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
//        Assertions.assertEquals(expectedCategories, persistedVideo.getCategories());
//        Assertions.assertEquals(expectedGenres, persistedVideo.getGenres());
//        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembers());
//        Assertions.assertEquals(expectedVideo.getName(), persistedVideo.getVideo().getName());
//        Assertions.assertEquals(expectedTrailer.getName(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.getName(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.getName(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.getName(), persistedVideo.getThumbnailHalf().getName());
    }

    @Test
    @Transactional
    void givenAValidVideoWithoutRelations_whenCreating_thenShouldPersistIt() {
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

        final var video = new VideoBuilder()
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
                .newVideo();

        final var createdVideo = videoGateway.create(video);

        Assertions.assertNotNull(createdVideo);
        Assertions.assertNotNull(createdVideo.getId());
        Assertions.assertEquals(expectedTitle, createdVideo.getTitle());
        Assertions.assertEquals(expectedDescription, createdVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, createdVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, createdVideo.getDuration());
        Assertions.assertEquals(expectedOpened, createdVideo.isOpened());
        Assertions.assertEquals(expectedPublished, createdVideo.isPublished());
        Assertions.assertEquals(expectedRating, createdVideo.getRating());
        Assertions.assertEquals(expectedCategories, createdVideo.getCategories());
        Assertions.assertEquals(expectedGenres, createdVideo.getGenres());
        Assertions.assertEquals(expectedMembers, createdVideo.getCastMembers());
        Assertions.assertTrue(createdVideo.getContent().isEmpty());
        Assertions.assertTrue(createdVideo.getTrailer().isEmpty());
        Assertions.assertTrue(createdVideo.getBanner().isEmpty());
        Assertions.assertTrue(createdVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(createdVideo.getThumbnailHalf().isEmpty());

        final var persistedVideo = videoRepository.findById(createdVideo.getId().getValue()).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategories());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenres());
        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembers());
        Assertions.assertNull(persistedVideo.getVideo());
        Assertions.assertNull(persistedVideo.getTrailer());
        Assertions.assertNull(persistedVideo.getBanner());
        Assertions.assertNull(persistedVideo.getThumbnail());
        Assertions.assertNull(persistedVideo.getThumbnailHalf());
    }

    @Test
    @Transactional
    void givenAValidVideo_whenUpdating_thenShouldPersistIt() {
        final var video = new VideoBuilder()
                .withTitle(Fixture.Videos.title())
                .withDescription(Fixture.Videos.description())
                .withLaunchedAt(Fixture.Videos.year())
                .withDuration(Fixture.Videos.duration())
                .withOpened(Fixture.bool())
                .withPublished(Fixture.bool())
                .withRating(Fixture.Videos.rating())
                .withCategories(Set.of())
                .withGenres(Set.of())
                .withCastMembers(Set.of())
                .newVideo();

        final var createdVideo = videoGateway.create(video);

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = true;
        final var expectedPublished = true;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(filmes.getId());
        final var expectedGenres = Set.of(acao.getId());
        final var expectedMembers = Set.of(zelda.getId());
        final var expectedVideo = VideoMedia.with("123", "video", "/media/video");
        final var expectedTrailer = VideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

        final var updatedVideo = Video.clone(createdVideo)
                .updateTitle(expectedTitle)
                .updateDescription(expectedDescription)
                .updateLaunchedAt(expectedLaunchYear)
                .updateDuration(expectedDuration)
                .open()
                .publish()
                .updateRating(expectedRating)
                .updateCategories(expectedCategories)
                .updateGenres(expectedGenres)
                .updateCastMembers(expectedMembers)
                .updateContent(expectedVideo)
                .updateTrailer(expectedTrailer)
                .updateBanner(expectedBanner)
                .updateThumbnail(expectedThumb)
                .updateThumbnailHalf(expectedThumbHalf);

        final var finalVideo = videoGateway.update(updatedVideo);

        Assertions.assertNotNull(finalVideo);
        Assertions.assertNotNull(finalVideo.getId());

        Assertions.assertEquals(expectedTitle, finalVideo.getTitle());
        Assertions.assertEquals(expectedDescription, finalVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, finalVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, finalVideo.getDuration());
        Assertions.assertEquals(expectedOpened, finalVideo.isOpened());
        Assertions.assertEquals(expectedPublished, finalVideo.isPublished());
        Assertions.assertEquals(expectedRating, finalVideo.getRating());
        Assertions.assertEquals(expectedCategories, finalVideo.getCategories());
        Assertions.assertEquals(expectedGenres, finalVideo.getGenres());
        Assertions.assertEquals(expectedMembers, finalVideo.getCastMembers());
        Assertions.assertEquals(expectedVideo.getName(), finalVideo.getContent().get().getName());
        Assertions.assertEquals(expectedTrailer.getName(), finalVideo.getTrailer().get().getName());
        Assertions.assertEquals(expectedBanner.getName(), finalVideo.getBanner().get().getName());
        Assertions.assertEquals(expectedThumb.getName(), finalVideo.getThumbnail().get().getName());
        Assertions.assertEquals(expectedThumbHalf.getName(), finalVideo.getThumbnailHalf().get().getName());
        Assertions.assertNotNull(finalVideo.getCreatedAt());
//        Assertions.assertTrue(finalVideo.getUpdatedAt().isAfter(createdVideo.getUpdatedAt()));

        final var persistedVideo = videoRepository.findById(finalVideo.getId().getValue()).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
//        Assertions.assertEquals(expectedCategories, persistedVideo.getCategories());
//        Assertions.assertEquals(expectedGenres, persistedVideo.getGenres());
//        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembers());
        Assertions.assertEquals(expectedVideo.getName(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.getName(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.getName(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.getName(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.getName(), persistedVideo.getThumbnailHalf().getName());
        Assertions.assertNotNull(persistedVideo.getCreatedAt());
//        Assertions.assertTrue(persistedVideo.getUpdatedAt().isAfter(createdVideo.getUpdatedAt()));
    }

    @Test
    void givenAValidVideoId_whenDeleting_thenShouldDeleteIt() {
        final var video = new VideoBuilder()
                .withTitle(Fixture.Videos.title())
                .withDescription(Fixture.Videos.description())
                .withLaunchedAt(Fixture.Videos.year())
                .withDuration(Fixture.Videos.duration())
                .withOpened(Fixture.bool())
                .withPublished(Fixture.bool())
                .withRating(Fixture.Videos.rating())
                .withCategories(Set.of())
                .withGenres(Set.of())
                .withCastMembers(Set.of())
                .newVideo();

        final var createdVideo = videoGateway.create(video);

        Assertions.assertEquals(1, videoRepository.count());

        videoGateway.deleteById(createdVideo.getId());

        Assertions.assertEquals(0, videoRepository.count());
    }

    @Test
    void givenAnInvalidVideoId_whenDeleting_thenShouldDeleteIt() {
        final var video = new VideoBuilder()
                .withTitle(Fixture.Videos.title())
                .withDescription(Fixture.Videos.description())
                .withLaunchedAt(Fixture.Videos.year())
                .withDuration(Fixture.Videos.duration())
                .withOpened(Fixture.bool())
                .withPublished(Fixture.bool())
                .withRating(Fixture.Videos.rating())
                .withCategories(Set.of())
                .withGenres(Set.of())
                .withCastMembers(Set.of())
                .newVideo();

        videoGateway.create(video);

        Assertions.assertEquals(1, videoRepository.count());

        videoGateway.deleteById(VideoID.unique());

        Assertions.assertEquals(1, videoRepository.count());
    }

    @Test
    void givenAValidVideo_whenFindingByID_thenShouldReturnIt() {
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(filmes.getId());
        final var expectedGenres = Set.of(acao.getId());
        final var expectedMembers = Set.of(zelda.getId());
        final var expectedVideo = VideoMedia.with("123", "video", "/media/video");
        final var expectedTrailer = VideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");
        final var decimalFormat = new DecimalFormat("#.00");
        final var formattedDuration = Double.parseDouble(decimalFormat.format(expectedDuration).replace(",", "."));

        final var video = new VideoBuilder()
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
                .newVideo()
                .updateContent(expectedVideo)
                .updateTrailer(expectedTrailer)
                .updateBanner(expectedBanner)
                .updateThumbnail(expectedThumb)
                .updateThumbnailHalf(expectedThumbHalf);

        final var createdVideo = videoGateway.create(video);
        final var foundVideo = videoGateway.findById(createdVideo.getId()).get();

        Assertions.assertNotNull(foundVideo);
        Assertions.assertNotNull(foundVideo.getId());
        Assertions.assertEquals(expectedTitle, foundVideo.getTitle());
        Assertions.assertEquals(expectedDescription, foundVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, foundVideo.getLaunchedAt());
//        Assertions.assertEquals(formattedDuration, foundVideo.getDuration());
        Assertions.assertEquals(expectedOpened, foundVideo.isOpened());
        Assertions.assertEquals(expectedPublished, foundVideo.isPublished());
        Assertions.assertEquals(expectedRating, foundVideo.getRating());
        Assertions.assertEquals(expectedCategories, foundVideo.getCategories());
        Assertions.assertEquals(expectedGenres, foundVideo.getGenres());
        Assertions.assertEquals(expectedMembers, foundVideo.getCastMembers());
        Assertions.assertEquals(expectedVideo.getName(), foundVideo.getContent().get().getName());
        Assertions.assertEquals(expectedTrailer.getName(), foundVideo.getTrailer().get().getName());
        Assertions.assertEquals(expectedBanner.getName(), foundVideo.getBanner().get().getName());
        Assertions.assertEquals(expectedThumb.getName(), foundVideo.getThumbnail().get().getName());
        Assertions.assertEquals(expectedThumbHalf.getName(), foundVideo.getThumbnailHalf().get().getName());
    }

    @Test
    void givenAnInvalidVideoId_whenFindingById_thenShouldEmpty() {
        final var video = new VideoBuilder()
                .withTitle(Fixture.Videos.title())
                .withDescription(Fixture.Videos.description())
                .withLaunchedAt(Fixture.Videos.year())
                .withDuration(Fixture.Videos.duration())
                .withOpened(Fixture.bool())
                .withPublished(Fixture.bool())
                .withRating(Fixture.Videos.rating())
                .withCategories(Set.of())
                .withGenres(Set.of())
                .withCastMembers(Set.of())
                .newVideo();

        this.videoGateway.create(video);
        final var foundVideo = videoGateway.findById(VideoID.unique());

        Assertions.assertTrue(foundVideo.isEmpty());
    }

    @Test
    void givenEmptyParams_whenListing_thenShouldReturnAllList() {
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 5;

        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of());

        final var page = this.videoGateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedTotal, page.items().size());
    }

    @Test
    void givenEmptyVideos_whenListing_thenShouldReturnEmptyList() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of());

        final var page = videoGateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedTotal, page.items().size());
    }

    @Test
    void givenAValidCategory_whenListing_thenShouldReturnFilteredList() {
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 3;

        Assertions.assertEquals(5, videoRepository.count());

        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(filmes.getId()), Set.of());

        final var page = videoGateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedTotal, page.items().size());
        Assertions.assertEquals("A volta dos que não foram", page.items().get(0).title());
        Assertions.assertEquals("O Enigma dos números", page.items().get(1).title());
    }

    @Test
    void givenAValidCastMember_whenListing_thenShouldReturnFilteredList() {
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 4;
        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(zelda.getId()), Set.of(), Set.of());

        final var page = videoGateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedTotal, page.items().size());
        Assertions.assertEquals("A volta dos que não foram", page.items().get(0).title());
        Assertions.assertEquals("O Enigma dos números", page.items().get(1).title());
    }

    @Test
    void givenAValidGenre_whenListing_thenShouldReturnFilteredList() {
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 3;
        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of(comedia.getId()));

        final var page = videoGateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedTotal, page.items().size());

        Assertions.assertEquals("O Enigma dos números", page.items().get(0).title());
    }

    @Test
    void givenAllParameters_whenListing_thenShouldReturnFilteredList() {
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "olhar";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;
        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(zelda.getId()), Set.of(series.getId()), Set.of(comedia.getId()));

        final var page = videoGateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedTotal, page.items().size());
        Assertions.assertEquals("O triste olhar de um pobre cego", page.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,A volta dos que não foram",
            "1,2,2,5,O triste olhar de um pobre cego;Poeira em alto mar",
    })
    void givenAValidPaging_whenListing_thenShouldReturnPaged(final int expectedPage, final int expectedPerPage,
                                                             final int expectedItemsCount, final long expectedTotal, final String expectedVideos) {

        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of());

        final var page = videoGateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedItemsCount, page.items().size());

        var index = 0;
        for (final var expectedTitle : expectedVideos.split(";")) {
            final var actualTitle = page.items().get(index++).title();
            Assertions.assertEquals(expectedTitle, actualTitle);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "enigma,0,10,1,1,O Enigma dos números",
            "olhar,0,10,1,1,O triste olhar de um pobre cego",
            "volta,0,10,1,1,A volta dos que não foram",
            "mar,0,10,1,1,Poeira em alto mar",
    })
    void givenAValidTerm_whenListing_thenShouldReturnFiltered(final String expectedTerms, final int expectedPage,
                                                              final int expectedPerPage, final int expectedItemsCount, final long expectedTotal,
                                                              final String expectedVideo) {

        mockVideos();

        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of());

        final var page = videoGateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedItemsCount, page.items().size());
        Assertions.assertEquals(expectedVideo, page.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,5,5,A volta dos que não foram",
            "title,desc,0,10,5,5,Prefiro a morte do que morrer",
    })
    void givenAValidSortAndDirection_whenListing_thenShouldReturnOrdered(final String expectedSort,
                                                                         final String expectedDirection, final int expectedPage, final int expectedPerPage,
                                                                         final int expectedItemsCount, final long expectedTotal, final String expectedVideo) {

        mockVideos();

        final var expectedTerms = "";
        final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of());

        final var page = videoGateway.findAll(query);

        Assertions.assertEquals(expectedPage, page.currentPage());
        Assertions.assertEquals(expectedPerPage, page.perPage());
        Assertions.assertEquals(expectedTotal, page.total());
        Assertions.assertEquals(expectedItemsCount, page.items().size());
        Assertions.assertEquals(expectedVideo, page.items().get(0).title());
    }

    private void mockVideos() {
        final var enigmaDosNúmeros = new VideoBuilder()
                .withTitle("O Enigma dos números")
                .withDescription("""
                        Inspirada pela mãe enquanto jogava um jogo de celular com a prima Ammanda, Julia, 
                        uma estudante de mestrado que trabalha em home office, tem que decifrar O ENIGMA DOS NÚMEROS 
                        para poder concluir o seu mestrado e voltar a ter vida.""")
                .withLaunchedAt(Year.of(2022))
                .withDuration(120)
                .withOpened(true)
                .withPublished(true)
                .withRating(Rating.L)
                .withCategories(Set.of(filmes.getId()))
                .withGenres(Set.of(comedia.getId()))
                .withCastMembers(Set.of(zelda.getId(), peach.getId()))
                .newVideo();
        videoGateway.create(enigmaDosNúmeros);

        final var poeiraEmAltoMar = new VideoBuilder()
                .withTitle("Poeira em alto mar")
                .withDescription("""
                        Ao se deparar com uma cena inexplicável o capitão de um navio precisa descobrir qual o motivo 
                        de estar preso com sua tripulação em um local no meio do oceano pacífico rodeado de poeira.""")
                .withLaunchedAt(Year.of(2021))
                .withDuration(100)
                .withOpened(false)
                .withPublished(true)
                .withRating(Rating.AGE_10)
                .withCategories(Set.of(filmes.getId()))
                .withGenres(Set.of(acao.getId()))
                .withCastMembers(Set.of(zelda.getId(), peach.getId()))
                .newVideo();
        videoGateway.create(poeiraEmAltoMar);

        final var aVoltaDosQueNaoForam = new VideoBuilder()
                .withTitle("A volta dos que não foram")
                .withDescription("""
                        Após anos presos em uma estação espacial, a tripulação se prepara para voltar à Terra. O que os 
                        está incomodando no momento é a situação de duas crianças que nasceram nesse paríodo e agora não 
                        se sabe como serão recebidas.
                        """)
                .withLaunchedAt(Year.of(2020))
                .withDuration(80)
                .withOpened(false)
                .withPublished(true)
                .withRating(Rating.AGE_18)
                .withCategories(Set.of(filmes.getId()))
                .withGenres(Set.of(acao.getId()))
                .withCastMembers(Set.of(zelda.getId()))
                .newVideo();
        videoGateway.create(aVoltaDosQueNaoForam);

        final var prefiroAMorteDoQueMorrer = new VideoBuilder()
                .withTitle("Prefiro a morte do que morrer")
                .withDescription("""
                        Um casal dono de uma fazenda no interior de Minas Gerais é assombrado por um espírito vigantivo 
                        que era o antigo dono daquelas terras e que é conhecido por matar suas vítimas porém deixando 
                        que elas mesmas escolham como preferem morrer.""")
                .withLaunchedAt(Year.of(2019))
                .withDuration(80)
                .withOpened(false)
                .withPublished(true)
                .withRating(Rating.AGE_18)
                .withCategories(Set.of(series.getId()))
                .withGenres(Set.of(comedia.getId()))
                .withCastMembers(Set.of(peach.getId()))
                .newVideo();
        videoGateway.create(prefiroAMorteDoQueMorrer);

        final var oTristeOlharDeUmPobreCego = new VideoBuilder()
                .withTitle("O triste olhar de um pobre cego")
                .withDescription("""
                        Cego de nascensça, uma criança que queria fazer a diferença no mundo procura por uma cura para 
                        sua cegueira.""")
                .withLaunchedAt(Year.of(2018))
                .withDuration(60)
                .withOpened(false)
                .withPublished(true)
                .withRating(Rating.AGE_14)
                .withCategories(Set.of(series.getId()))
                .withGenres(Set.of(comedia.getId()))
                .withCastMembers(Set.of(zelda.getId(), peach.getId()))
                .newVideo();
        videoGateway.create(oTristeOlharDeUmPobreCego);
    }
}
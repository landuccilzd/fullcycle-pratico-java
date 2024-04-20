package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.UnitTest;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

class VideoValidatorTest extends UnitTest {

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
    private static final double[] EXPECTED_DURATIONS = { 115.0, 114.0 };
    private static final boolean[] EXPECTED_OPENED = { false, true };
    private static final boolean[] EXPECTED_PUBLISHED = { true, false };
    private static final Rating[] EXPECTED_RATINGS = { Rating.AGE_12, Rating.AGE_12 };

    @Test
    void givenANullTitle_whenValidatingVideo_thenShouldReceiveAnError() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Title should not be null";

        final var video = new VideoBuilder()
                .withId(VideoID.unique())
                .withTitle(null)
                .withDescription(EXPECTED_DESCRIPTIONS[0])
                .withLaunchedAt(EXPECTED_LAUNCHED_AT[0])
                .withDuration(EXPECTED_DURATIONS[0])
                .withOpened(EXPECTED_OPENED[0])
                .withPublished(EXPECTED_PUBLISHED[0])
                .withRating(EXPECTED_RATINGS[0])
                .withCategories(expectedCategories)
                .withGenres(expectedGenres)
                .withCastMembers(expectedMembers)
                .build();

        final var handler = new ThrowsValidationHandler();
        final var exception = Assertions.assertThrows(
                DomainException.class, () -> video.validate(handler)
        );

        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenAnEmptyTitle_whenValidating_thenShouldReceiveAnError() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Title should not be empty";

        final var video = new VideoBuilder()
                .withId(VideoID.unique())
                .withTitle("")
                .withDescription(EXPECTED_DESCRIPTIONS[0])
                .withLaunchedAt(EXPECTED_LAUNCHED_AT[0])
                .withDuration(EXPECTED_DURATIONS[0])
                .withOpened(EXPECTED_OPENED[0])
                .withPublished(EXPECTED_PUBLISHED[0])
                .withRating(EXPECTED_RATINGS[0])
                .withCategories(expectedCategories)
                .withGenres(expectedGenres)
                .withCastMembers(expectedMembers)
                .build();

        final var handler = new ThrowsValidationHandler();
        final var exception = Assertions.assertThrows(
                DomainException.class, () -> video.validate(handler)
        );

        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenALongTitle_whenValidating_thenShouldReceiveAnError() {
        final var expectedTitle = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et 
            dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex 
            ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu 
            fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt 
            mollit anim id est laborum.
        """;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Title must have less then 255 characters";

        final var video = new VideoBuilder()
                .withId(VideoID.unique())
                .withTitle(expectedTitle)
                .withDescription(EXPECTED_DESCRIPTIONS[0])
                .withLaunchedAt(EXPECTED_LAUNCHED_AT[0])
                .withDuration(EXPECTED_DURATIONS[0])
                .withOpened(EXPECTED_OPENED[0])
                .withPublished(EXPECTED_PUBLISHED[0])
                .withRating(EXPECTED_RATINGS[0])
                .withCategories(expectedCategories)
                .withGenres(expectedGenres)
                .withCastMembers(expectedMembers)
                .build();

        final var handler = new ThrowsValidationHandler();
        final var exception = Assertions.assertThrows(
                DomainException.class, () -> video.validate(handler)
        );

        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenAnEmptyDescription_whenValidating_thenShouldReceiveError() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Description should not be empty";

        final var video = new VideoBuilder()
                .withId(VideoID.unique())
                .withTitle(EXPECTED_TITLES[0])
                .withDescription("")
                .withLaunchedAt(EXPECTED_LAUNCHED_AT[0])
                .withDuration(EXPECTED_DURATIONS[0])
                .withOpened(EXPECTED_OPENED[0])
                .withPublished(EXPECTED_PUBLISHED[0])
                .withRating(EXPECTED_RATINGS[0])
                .withCategories(expectedCategories)
                .withGenres(expectedGenres)
                .withCastMembers(expectedMembers)
                .build();

        final var handler = new ThrowsValidationHandler();
        final var exception = Assertions.assertThrows(
                DomainException.class, () -> video.validate(handler)
        );

        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenALongDescription_whenValidating_thenShouldReceiveAnError() {
        final var expectedDescription = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque eu auctor leo. Donec id iaculis tellus, 
            et pellentesque magna. Aliquam erat volutpat. Pellentesque in commodo leo, vel euismod lectus. Nulla cursus 
            vel lacus a euismod. Sed aliquam odio ut lacus imperdiet volutpat. In egestas aliquet viverra. Vivamus 
            volutpat, felis et tincidunt facilisis, tortor nulla eleifend lectus, eu suscipit odio neque quis mi. 
            Cras volutpat lacus non nunc aliquet, lobortis blandit nunc dapibus. Vestibulum eu elit rutrum, aliquet 
            neque nec, aliquam augue. Sed sit amet venenatis dui. Nulla facilisi. In sed dapibus eros.
            
            Nam fermentum ex ut nulla volutpat mollis ac eget lacus. Nulla facilisi. Praesent nec metus non sem auctor 
            finibus. Duis at suscipit metus. Sed ac mi pulvinar, eleifend metus in, venenatis ante. Fusce efficitur est 
            eget lacus condimentum, non hendrerit lacus venenatis. Praesent quis velit quis leo viverra viverra eu et 
            sem. Suspendisse facilisis id risus eget hendrerit. Donec dui turpis, venenatis et rutrum vitae, volutpat 
            porta mauris. Etiam auctor vel tellus non dapibus. Fusce lobortis, lectus ac vehicula elementum, quam mi 
            cursus libero, vel cursus nibh dui nec risus. Sed tristique gravida velit. Orci varius natoque penatibus et 
            magnis dis parturient montes, nascetur ridiculus mus.
            
            Aliquam sed metus laoreet, placerat erat quis, ullamcorper eros. Nulla eu tristique velit, ut vulputate 
            nunc. Nunc nec enim erat. Praesent congue augue laoreet magna accumsan scelerisque sed sed turpis. 
            Pellentesque aliquet posuere euismod. Nam ut purus pellentesque diam vestibulum posuere. Cras venenatis 
            pellentesque pharetra. Etiam eget eros ut ligula tristique eleifend mattis ac libero. Cras tincidunt ac 
            libero iaculis porttitor. Curabitur hendrerit, ex eget placerat vestibulum, ante tortor viverra ante, sed 
            porta magna magna ut purus. Vivamus quis bibendum sem. Morbi congue turpis at augue mollis pellentesque. Ut 
            est magna, feugiat ac placerat sit amet, ullamcorper vitae enim. Sed quis ultrices tortor, a vehicula 
            sapien. Nam ullamcorper commodo dolor.
            
            Maecenas at ipsum in lacus consectetur dapibus. Ut arcu arcu, dignissim non magna sed, venenatis convallis 
            nisi. Maecenas id enim nisi. Quisque id nibh nec leo volutpat porttitor ac eu felis. Nunc sollicitudin lacus 
            mauris, vel tempus arcu pellentesque vel. Quisque suscipit purus commodo diam laoreet semper. Aenean 
            vestibulum lorem et vulputate semper. Quisque aliquet ante at placerat ultrices. Etiam sit amet laoreet 
            tellus. Ut in rhoncus ligula, ut sodales enim. Donec accumsan est at dictum convallis. Pellentesque enim 
            lacus, luctus rhoncus congue ac, blandit eu arcu. Nunc auctor congue enim sed venenatis. Mauris elit orci, 
            vestibulum vitae fringilla ac, aliquam ornare ipsum.
            
            Nunc libero lectus, mattis id eleifend sit amet, feugiat vitae nunc. Sed cursus turpis posuere, feugiat 
            diam eu, vehicula odio. Morbi pharetra laoreet erat sed rutrum. Aliquam semper, augue a ullamcorper 
            imperdiet, magna risus facilisis lectus, at porttitor sem arcu ac neque. Aliquam egestas ultrices purus et 
            posuere. Etiam ut eleifend orci. Fusce leo quam, maximus eget fermentum ac, sodales eget tortor. Vivamus 
            suscipit justo non lectus lacinia, vel dictum neque hendrerit. Curabitur maximus lectus quam, vel lobortis 
            elit rhoncus a.
            
            Sed in pulvinar enim. Nulla non mi volutpat, suscipit enim eu, gravida nisi. Morbi sagittis porttitor ex, 
            et eleifend libero cursus at. Maecenas leo urna, porttitor eget mauris id, varius pharetra lacus. Sed non 
            dignissim orci, in fringilla nisl. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam risus 
            massa, faucibus id sapien quis, interdum venenatis risus. Donec varius suscipit mauris nec venenatis. Sed 
            convallis tempus nisl, eget ultrices est.
            
            Phasellus tincidunt gravida lacinia. Duis eu ex vehicula diam semper tincidunt ut id ligula. 
            Praesent laoreet, odio in porttitor consectetur, dui risus commodo justo, at iaculis nulla velit in justo. 
            Duis vitae sodales eros. Curabitur massa ligula, vestibulum in luctus vel, tincidunt nec nibh. Phasellus 
            pulvinar felis nec nisi convallis, vel semper ipsum sodales. Vestibulum ante ipsum primis in faucibus orci 
            luctus et ultrices posuere cubilia curae; Ut placerat gravida ultrices. Etiam lorem orci, placerat eu lacus 
            id, feugiat lobortis nunc. Duis ut sapien in quam suscipit cursus id vitae leo. In viverra egestas leo, at 
            vehicula ante venenatis hendrerit. Donec laoreet maximus placerat.
            
            Phasellus consequat est efficitur, venenatis justo quis, malesuada urna. Proin tempor quam ut quam sagittis 
            sagittis. Duis dapibus nisl vel sapien finibus, in laoreet odio interdum. Suspendisse aliquet nisl et 
            tempor facilisis. Pellentesque egestas est et neque auctor, sed sodales nunc volutpat. Aliquam luctus quam 
            nibh, eu vehicula dui varius et. Nulla in aliquam neque, nec tincidunt tortor. Vivamus gravida quam id justo 
            egestas rutrum. Donec id tincidunt ipsum. Maecenas nec turpis vel justo dignissim hendrerit et at sapien. 
            Vivamus vestibulum.
        """;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Description must have less then 4000 characters";

        final var video = new VideoBuilder()
                .withId(VideoID.unique())
                .withTitle(EXPECTED_TITLES[0])
                .withDescription(expectedDescription)
                .withLaunchedAt(EXPECTED_LAUNCHED_AT[0])
                .withDuration(EXPECTED_DURATIONS[0])
                .withOpened(EXPECTED_OPENED[0])
                .withPublished(EXPECTED_PUBLISHED[0])
                .withRating(EXPECTED_RATINGS[0])
                .withCategories(expectedCategories)
                .withGenres(expectedGenres)
                .withCastMembers(expectedMembers)
                .build();

        final var handler = new ThrowsValidationHandler();
        final var exception = Assertions.assertThrows(
                DomainException.class, () -> video.validate(handler)
        );

        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenANullLaunchedAt_whenValidating_thenShouldReceiveAnError() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Launched At should not be null";

        final var video = new VideoBuilder()
                .withId(VideoID.unique())
                .withTitle(EXPECTED_TITLES[0])
                .withDescription(EXPECTED_DESCRIPTIONS[0])
                .withLaunchedAt(null)
                .withDuration(EXPECTED_DURATIONS[0])
                .withOpened(EXPECTED_OPENED[0])
                .withPublished(EXPECTED_PUBLISHED[0])
                .withRating(EXPECTED_RATINGS[0])
                .withCategories(expectedCategories)
                .withGenres(expectedGenres)
                .withCastMembers(expectedMembers)
                .build();

        final var handler = new ThrowsValidationHandler();
        final var exception = Assertions.assertThrows(
                DomainException.class, () -> video.validate(handler)
        );

        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenANullRating_whenValidating_thenShouldReceiveAnError() {
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Rating should not be null";

        final var video = new VideoBuilder()
                .withId(VideoID.unique())
                .withTitle(EXPECTED_TITLES[0])
                .withDescription(EXPECTED_DESCRIPTIONS[0])
                .withLaunchedAt(EXPECTED_LAUNCHED_AT[0])
                .withDuration(EXPECTED_DURATIONS[0])
                .withOpened(EXPECTED_OPENED[0])
                .withPublished(EXPECTED_PUBLISHED[0])
                .withRating(null)
                .withCategories(expectedCategories)
                .withGenres(expectedGenres)
                .withCastMembers(expectedMembers)
                .build();

        final var handler = new ThrowsValidationHandler();
        final var exception = Assertions.assertThrows(
                DomainException.class, () -> video.validate(handler)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

}
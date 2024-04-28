package br.landucci.admin.catologo.domain;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.utils.IdUtils;
import br.landucci.admin.catologo.domain.video.*;
import com.github.javafaker.Faker;
import io.vavr.API;

import java.time.Year;
import java.util.Set;
import java.util.UUID;

public final class Fixture {

    public static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static final class Categories {
        private static final Category FILMES =
                Category.newCategory("Filmes", "Reúne todos os filmes do catálogo", true);

        private static final Category SERIES =
                Category.newCategory("Séries", "Reúne todas as séries do catálogo", true);

        private static final Category DOCUMENTARIO =
                Category.newCategory("Documentários", "Reúne todos os documentarios do catálogo", true);

        public static Category filmes() {
            return Category.clone(FILMES);
        }

        public static Category series() {
            return Category.clone(SERIES);
        }

        public static Category documentarios() {
            return Category.clone(DOCUMENTARIO);
        }
    }

    public static final class Genres {
        private static final Genre ACAO = Genre.newGenre("Ação", true);
        private static final Genre TERROR = Genre.newGenre("Terror", true);
        private static final Genre SUSPENSE = Genre.newGenre("Suspense", true);
        private static final Genre DRAMA = Genre.newGenre("Drama", true);
        private static final Genre COMEDIA = Genre.newGenre("Comédia", true);
        private static final Genre FICCAO_CIENTIFICA = Genre.newGenre("Ficção Científica", true);

        public static Genre acao() {
            return Genre.clone(ACAO);
        }

        public static Genre terror() {
            return Genre.clone(TERROR);
        }

        public static Genre suspense() {
            return Genre.clone(SUSPENSE);
        }

        public static Genre drama() {
            return Genre.clone(DRAMA);
        }

        public static Genre comedia() {
            return Genre.clone(COMEDIA);
        }

        public static Genre ficcaoCientifica() {
            return Genre.clone(FICCAO_CIENTIFICA);
        }
    }

    public static final class CastMembers {

        private static final CastMember JEHNIFFER_ANISTON =
                CastMember.newCastMember("Jehniffer Aniston", CastMemberType.ACTOR);

        private static final CastMember MEGAN_FOX =
                CastMember.newCastMember("Megan Fox", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }

        public static CastMember jehnifferAniston() {
            return CastMember.clone(JEHNIFFER_ANISTON);
        }

        public static CastMember meganFox() {
            return CastMember.clone(MEGAN_FOX);
        }
    }

    public static final class Videos {

        public static String title() {
            return FAKER.options().option(
                    "O Enigma dos números",
                    "Poeira em alto mar",
                    "A volta dos que não foram",
                    "Prefiro a morte do que morrer",
                    "O triste olhar de um pobre cego"
            );
        }

        public static String description() {
            return FAKER.options().option(
                    """
                Inspirada pela mãe enquanto jogava um jogo de celular com a prima Ammanda, Julia, uma estudante de mestrado
                que trabalha em home office, tem que decifrar O ENIGMA DOS NÚMEROS para poder concluir o seu mestrado e 
                voltar a ter vida.
            """,
                    """
                        Ao se deparar com uma cena inexplicável o capitão de um navio precisa descobrir qual o motivo de estar 
                        preso com sua tripulação em um local no meio do oceano pacífico rodeado de poeira.
                    """,
                    """
                        Após anos presos em uma estação espacial, a tripulação se prepara para voltar à Terra. O que os está 
                        incomodando no momento é a situação de duas crianças que nasceram nesse paríodo e agora não se sabe como 
                        serão recebidas.
                    """,
                    """
                        Um casal dono de uma fazendo no interior de Minas Gerais é assombrado por um espírito vigantivo que era o 
                        antigo dono daquelas terras e que é conhecido por matar suas vítimas porém deixando que elas mesmas escolham
                        como preferem morrer.
                    """,
                    """
                        Cego de nascensça, uma criança que queria fazer a diferença no mundo procura por uma cura para sua cegueira.
                    """
            );
        }

        public static Year year() {
            final var ano =  FAKER.random().nextInt(1980, 2024);
            return Year.of(ano);
        }

        public static double duration() {
            return FAKER.options().option(80.0, 90.0, 100.0, 110.0, 120.0);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static String checksum() {
            return UUID.randomUUID().toString().toLowerCase();
        }

        public static VideoMediaType mediaType() {
            return FAKER.options().option(VideoMediaType.values());
        }

        public static Video video() {
            return new VideoBuilder().withId(VideoID.unique())
                    .withTitle(title())
                    .withDescription(description())
                    .withLaunchedAt(year())
                    .withDuration(duration())
                    .withOpened(Fixture.bool())
                    .withPublished(Fixture.bool())
                    .withRating(Videos.rating())
                    .withCategories(Set.of(Categories.filmes().getId()))
                    .withGenres(Set.of(Genres.ficcaoCientifica().getId()))
                    .withCastMembers(Set.of(CastMembers.jehnifferAniston().getId(), CastMembers.meganFox().getId()))
                    .build();
        }

        public static Resource resource(final VideoMediaType type) {
            final String contentType = API.Match(type).of(
                    API.Case(API.$(API.List(VideoMediaType.VIDEO, VideoMediaType.TRAILER)::contains), "video/mp4"),
                    API.Case(API.$(), "image/jpg")
            );

            final String checksum = IdUtils.uuid();
            final byte[] content = "Conteudo".getBytes();

            return Resource.with(content, checksum, contentType, type.name().toLowerCase());
        }

        public static VideoMedia audioVideo(final VideoMediaType type) {
            final var checksum = checksum();
            return VideoMedia.with( checksum, type.name().toLowerCase(), "/videos/" + checksum
            );
        }

        public static ImageMedia image(final VideoMediaType type) {
            final var checksum = checksum();
            return ImageMedia.with(checksum, type.name().toLowerCase(), "/images/" + checksum
            );
        }
    }
}

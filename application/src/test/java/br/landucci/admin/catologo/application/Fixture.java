package br.landucci.admin.catologo.application;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.utils.IdUtils;
import br.landucci.admin.catologo.domain.video.*;
import io.vavr.API;

import java.time.Year;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public final class Fixture {

    private static final String[] NAMES = new String[] { "Zelda", "Peach", "Katara", "She Ra", "Kitana" };
    private static final boolean[] BOOLS = new boolean[] { true, false };

    private static final Rating[] RATINGS = new Rating[] { Rating.L, Rating.ER, Rating.AGE_10, Rating.AGE_12,
            Rating.AGE_14, Rating.AGE_16, Rating.AGE_18 };

    private static final VideoMediaType[] MEDIA_TYPES = new VideoMediaType[] {VideoMediaType.VIDEO,
            VideoMediaType.TRAILER, VideoMediaType.BANNER, VideoMediaType.THUMBNAIL, VideoMediaType.THUMBNAIL_HALF };

    private static final CastMemberType[] CAST_MEMBER_TYPES = new CastMemberType[] { CastMemberType.ACTOR,
            CastMemberType.DIRECTOR };

    private static final String[] TITLES = new String[] {"O Enigma dos números", "Poeira em alto mar",
            "A volta dos que não foram", "Prefiro a morte do que morrer", "O triste olhar de um pobre cego" };

    private static final String[] DESCRIPTIONS = new String[] {
            """
                Inspirada pela mãe enquanto jogava um jogo de celular com a prima Ammanda, Julia, uma estudante de 
                mestrado que trabalha em home office, tem que decifrar O ENIGMA DOS NÚMEROS para poder concluir o seu 
                mestrado e voltar a ter vida.
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
                Um casal dono de uma fazendo no interior de Minas Gerais é assombrado por um espírito vigantivo que era 
                o antigo dono daquelas terras e que é conhecido por matar suas vítimas porém deixando que elas mesmas 
                escolham como preferem morrer.
            """,
            """
                Cego de nascensça, uma criança que queria fazer a diferença no mundo procura por uma cura para sua 
                cegueira.
            """
    };

    private static final Random RANDOM = new Random();


    public static String name() {
        final var index = RANDOM.nextInt(NAMES.length);
        return NAMES[index];
    }

    public static boolean bool() {
        final var index = RANDOM.nextInt(BOOLS.length);
        return BOOLS[index];
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

        private static final CastMember ZELDA =
                CastMember.newCastMember("Zelda", CastMemberType.ACTOR);

        private static final CastMember PEACH =
                CastMember.newCastMember("Peach", CastMemberType.ACTOR);

        public static CastMemberType type() {
            final var index = RANDOM.nextInt(CAST_MEMBER_TYPES.length);
            return CAST_MEMBER_TYPES[index];
        }

        public static CastMember zelda() {
            return CastMember.clone(ZELDA);
        }

        public static CastMember peach() {
            return CastMember.clone(PEACH);
        }
    }

    public static final class Videos {

        public static String title() {
            final var index = RANDOM.nextInt(TITLES.length);
            return TITLES[index];
        }

        public static String description() {
            final var index = RANDOM.nextInt(DESCRIPTIONS.length);
            return DESCRIPTIONS[index];
        }

        public static Year year() {
            final var ano = RANDOM.nextInt(1980, 2024);
            return Year.of(ano);
        }

        public static double duration() {
            return RANDOM.nextDouble(60, 120);
        }

        public static Rating rating() {
            final var index =  RANDOM.nextInt(RATINGS.length);
            return RATINGS[index];
        }

        public static String checksum() {
            return UUID.randomUUID().toString().toLowerCase();
        }

        public static VideoMediaType mediaType() {
            final var index = RANDOM.nextInt(MEDIA_TYPES.length);
            return MEDIA_TYPES[index];
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
                    .withCastMembers(Set.of(CastMembers.zelda().getId(), CastMembers.peach().getId()))
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

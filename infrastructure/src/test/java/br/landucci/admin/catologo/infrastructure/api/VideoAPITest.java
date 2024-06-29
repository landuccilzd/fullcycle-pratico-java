package br.landucci.admin.catologo.infrastructure.api;

import br.landucci.admin.catologo.ApiTest;
import br.landucci.admin.catologo.ControllerTest;
import br.landucci.admin.catologo.application.video.create.CreateVideoInputCommand;
import br.landucci.admin.catologo.application.video.create.CreateVideoOutputCommand;
import br.landucci.admin.catologo.application.video.create.CreateVideoUseCase;
import br.landucci.admin.catologo.application.video.delete.DeleteVideoUseCase;
import br.landucci.admin.catologo.application.video.find.FindVideoByIDOutputCommand;
import br.landucci.admin.catologo.application.video.find.FindVideoByIDUseCase;
import br.landucci.admin.catologo.application.video.list.ListVideoOutputCommand;
import br.landucci.admin.catologo.application.video.list.ListVideosUseCase;
import br.landucci.admin.catologo.application.video.media.find.FindMediaByIDInputCommand;
import br.landucci.admin.catologo.application.video.media.find.FindMediaByIDOutputCommand;
import br.landucci.admin.catologo.application.video.media.find.FindMediaByIDUseCase;
import br.landucci.admin.catologo.application.video.media.upload.UploadMediaInputCommand;
import br.landucci.admin.catologo.application.video.media.upload.UploadMediaOutputCommand;
import br.landucci.admin.catologo.application.video.media.upload.UploadMediaUseCase;
import br.landucci.admin.catologo.application.video.update.UpdateVideoInputCommand;
import br.landucci.admin.catologo.application.video.update.UpdateVideoOutputCommand;
import br.landucci.admin.catologo.application.video.update.UpdateVideoUseCase;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.utils.CollectionUtils;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.video.*;
import br.landucci.admin.catologo.infrastructure.Fixture;
import br.landucci.admin.catologo.infrastructure.video.models.CreateVideoRequestCommand;
import br.landucci.admin.catologo.infrastructure.video.models.UpdateVideoRequestCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerTest(controllers = VideoAPI.class)
class VideoAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

    @MockBean
    private FindVideoByIDUseCase findVideoByIDUseCase;

    @MockBean
    private UpdateVideoUseCase updateVideoUseCase;

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private ListVideosUseCase listVideosUseCase;

    @MockBean
    private FindMediaByIDUseCase findMediaByIDUseCase;

    @MockBean
    private UploadMediaUseCase uploadMediaUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateFull_shouldReturnAnId() throws Exception {
        final var peach = Fixture.CastMembers.peach();
        final var filmes = Fixture.Categories.filmes();
        final var acao = Fixture.Genres.acao();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(filmes.getId().getValue());
        final var expectedGenres = Set.of(acao.getId().getValue());
        final var expectedMembers = Set.of(peach.getId().getValue());

        final var expectedVideo =
                new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());

        final var expectedTrailer =
                new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".getBytes());

        final var expectedBanner =
                new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());

        final var expectedThumb =
                new MockMultipartFile("thumb_file", "thumbnail.jpg", "image/jpg", "THUMB".getBytes());

        final var expectedThumbHalf =
                new MockMultipartFile("thumb_half_file", "thumbnailHalf.jpg", "image/jpg", "THUMBHALF".getBytes());

        Mockito.when(createVideoUseCase.execute(Mockito.any())).thenReturn(new CreateVideoOutputCommand(expectedId.getValue()));

        final var aRequest = MockMvcRequestBuilders.multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumb)
                .file(expectedThumbHalf)
                .with(ApiTest.VIDEOS_JWT)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchYear.getValue()))
                .param("duration", String.valueOf(expectedDuration))
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("rating", expectedRating.getName())
                .param("cast_members_id", peach.getId().getValue())
                .param("categories_id", filmes.getId().getValue())
                .param("genres_id", acao.getId().getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        final var captor = ArgumentCaptor.forClass(CreateVideoInputCommand.class);

        Mockito.verify(createVideoUseCase).execute(captor.capture());

        final var command = captor.getValue();

        Assertions.assertEquals(expectedTitle, command.title());
        Assertions.assertEquals(expectedDescription, command.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), command.launchedAt());
        Assertions.assertEquals(expectedDuration, command.duration());
        Assertions.assertEquals(expectedOpened, command.opened());
        Assertions.assertEquals(expectedPublished, command.published());
        Assertions.assertEquals(expectedRating.getName(), command.rating());
        Assertions.assertEquals(expectedCategories, command.categories());
        Assertions.assertEquals(expectedGenres, command.genres());
        Assertions.assertEquals(expectedMembers, command.members());
        Assertions.assertEquals(expectedVideo.getOriginalFilename(), command.getVideo().get().getName());
        Assertions.assertEquals(expectedTrailer.getOriginalFilename(), command.getTrailer().get().getName());
        Assertions.assertEquals(expectedBanner.getOriginalFilename(), command.getBanner().get().getName());
        Assertions.assertEquals(expectedThumb.getOriginalFilename(), command.getThumbnail().get().getName());
        Assertions.assertEquals(expectedThumbHalf.getOriginalFilename(), command.getThumbnailHalf().get().getName());
    }

    @Test
    void givenAnInvalidCommand_whenCallsCreateFull_shouldReturnError() throws Exception {
        final var expectedErrorMessage = "title is required";

        Mockito.when(createVideoUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new ValidationError(expectedErrorMessage)));

        final var aRequest = MockMvcRequestBuilders.multipart("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidCommand_whenCallsCreatePartial_shouldReturnId() throws Exception {
        final var zelda = Fixture.CastMembers.zelda();
        final var series = Fixture.Categories.series();
        final var comedia = Fixture.Genres.comedia();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(series.getId().getValue());
        final var expectedGenres = Set.of(comedia.getId().getValue());
        final var expectedMembers = Set.of(zelda.getId().getValue());

        final var command = new CreateVideoRequestCommand(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        Mockito.when(createVideoUseCase.execute(Mockito.any()))
                .thenReturn(new CreateVideoOutputCommand(expectedId.getValue()));

        final var aRequest = MockMvcRequestBuilders.post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        final var captor = ArgumentCaptor.forClass(CreateVideoInputCommand.class);

        Mockito.verify(createVideoUseCase).execute(captor.capture());

        final var actualCmd = captor.getValue();

        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedMembers, actualCmd.members());
        Assertions.assertTrue(actualCmd.getVideo().isEmpty());
        Assertions.assertTrue(actualCmd.getTrailer().isEmpty());
        Assertions.assertTrue(actualCmd.getBanner().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnail().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    void givenAnEmptyBody_whenCallsCreatePartial_shouldReturnError() throws Exception {
        final var aRequest = MockMvcRequestBuilders.post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenAnInvalidCommand_whenCallsCreatePartial_shouldReturnError() throws Exception {
        final var expectedErrorMessage = "title is required";

        Mockito.when(createVideoUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new ValidationError(expectedErrorMessage)));

        final var aRequest = MockMvcRequestBuilders.post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "Olá Mundo!"
                        }
                        """);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidId_whenCallsGetById_shouldReturnVideo() throws Exception {
        final var peach = Fixture.CastMembers.peach();
        final var documentarios = Fixture.Categories.documentarios();
        final var drama = Fixture.Genres.drama();

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(documentarios.getId().getValue());
        final var expectedGenres = Set.of(drama.getId().getValue());
        final var expectedMembers = Set.of(peach.getId().getValue());
        final var expectedVideo = Fixture.Videos.audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);


        final var video = new VideoBuilder()
                .withTitle(expectedTitle)
                .withDescription(expectedDescription)
                .withLaunchedAt(expectedLaunchYear)
                .withDuration(expectedDuration)
                .withOpened(expectedOpened)
                .withPublished(expectedPublished)
                .withRating(expectedRating)
                .withCategories(CollectionUtils.mapTo(expectedCategories, CategoryID::from))
                .withGenres(CollectionUtils.mapTo(expectedGenres, GenreID::from))
                .withCastMembers(CollectionUtils.mapTo(expectedMembers, CastMemberID::with))
                .newVideo();

        video.updateContent(expectedVideo)
                .updateTrailer(expectedTrailer)
                .updateBanner(expectedBanner)
                .updateThumbnail(expectedThumb)
                .updateThumbnailHalf(expectedThumbHalf);

        final var expectedId = video.getId().getValue();

        Mockito.when(findVideoByIDUseCase.execute(Mockito.any()))
                .thenReturn(FindVideoByIDOutputCommand.from(video));

        final var aRequest = MockMvcRequestBuilders.get("/videos/{id}", expectedId)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo(expectedTitle)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year_launched", Matchers.equalTo(expectedLaunchYear.getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", Matchers.equalTo(expectedDuration)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.opened", Matchers.equalTo(expectedOpened)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.published", Matchers.equalTo(expectedPublished)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating", Matchers.equalTo(expectedRating.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(video.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(video.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banner.id", Matchers.equalTo(expectedBanner.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banner.name", Matchers.equalTo(expectedBanner.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banner.location", Matchers.equalTo(expectedBanner.getLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banner.checksum", Matchers.equalTo(expectedBanner.getChecksum())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail.id", Matchers.equalTo(expectedThumb.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail.name", Matchers.equalTo(expectedThumb.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail.location", Matchers.equalTo(expectedThumb.getLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail.checksum", Matchers.equalTo(expectedThumb.getChecksum())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail_half.id", Matchers.equalTo(expectedThumbHalf.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail_half.name", Matchers.equalTo(expectedThumbHalf.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail_half.location", Matchers.equalTo(expectedThumbHalf.getLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail_half.checksum", Matchers.equalTo(expectedThumbHalf.getChecksum())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.id", Matchers.equalTo(expectedVideo.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.name", Matchers.equalTo(expectedVideo.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.checksum", Matchers.equalTo(expectedVideo.getChecksum())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.location", Matchers.equalTo(expectedVideo.getRawLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.encoded_location", Matchers.equalTo(expectedVideo.getEncodedLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.status", Matchers.equalTo(expectedVideo.getStatus().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.id", Matchers.equalTo(expectedTrailer.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.name", Matchers.equalTo(expectedTrailer.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.checksum", Matchers.equalTo(expectedTrailer.getChecksum())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.location", Matchers.equalTo(expectedTrailer.getRawLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.encoded_location", Matchers.equalTo(expectedTrailer.getEncodedLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.status", Matchers.equalTo(expectedTrailer.getStatus().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categories_id", Matchers.equalTo(new ArrayList(expectedCategories))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genres_id", Matchers.equalTo(new ArrayList(expectedGenres))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cast_members_id", Matchers.equalTo(new ArrayList(expectedMembers))));
    }

    @Test
    void givenAnInvalidId_whenCallsGetById_shouldReturnNotFound() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        Mockito.when(findVideoByIDUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Video.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/videos/{id}", expectedId)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() throws Exception {
        final var zelda = Fixture.CastMembers.zelda();
        final var filmes = Fixture.Categories.filmes();
        final var suspense = Fixture.Genres.suspense();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(filmes.getId().getValue());
        final var expectedGenres = Set.of(suspense.getId().getValue());
        final var expectedMembers = Set.of(zelda.getId().getValue());

        final var command = new UpdateVideoRequestCommand(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        Mockito.when(updateVideoUseCase.execute(Mockito.any())).thenReturn(new UpdateVideoOutputCommand(expectedId.getValue()));

        final var request = MockMvcRequestBuilders.put("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        final var captor = ArgumentCaptor.forClass(UpdateVideoInputCommand.class);

        Mockito.verify(updateVideoUseCase).execute(captor.capture());

        final var actualCmd = captor.getValue();

        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedMembers, actualCmd.members());
        Assertions.assertTrue(actualCmd.getVideo().isEmpty());
        Assertions.assertTrue(actualCmd.getTrailer().isEmpty());
        Assertions.assertTrue(actualCmd.getBanner().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnail().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    void givenAnInvalidCommand_whenCallsUpdateVideo_shouldReturnNotification() throws Exception {
        // given
        final var zelda = Fixture.CastMembers.zelda();
        final var series = Fixture.Categories.series();
        final var terror = Fixture.Genres.terror();

        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.Videos.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(series.getId().getValue());
        final var expectedGenres = Set.of(terror.getId().getValue());
        final var expectedMembers = Set.of(zelda.getId().getValue());

        final var command = new UpdateVideoRequestCommand(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        Mockito.when(updateVideoUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new ValidationError(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.put("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        // then
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateVideoUseCase).execute(Mockito.any());
    }

    @Test
    void givenAValidId_whenCallsDeleteById_shouldDeleteIt() throws Exception {
        final var expectedId = VideoID.unique();

        Mockito.doNothing().when(deleteVideoUseCase).execute(Mockito.any());

        final var request = MockMvcRequestBuilders.delete("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteVideoUseCase).execute(Mockito.eq(expectedId.getValue()));
    }

    @Test
    void givenValidParams_whenCallsListVideos_shouldReturnPagination() throws Exception {
        final var aVideo = new VideoPreview(Fixture.Videos.video());
        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "Algo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCastMembers = "cast1";
        final var expectedGenres = "gen1";
        final var expectedCategories = "cat1";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(ListVideoOutputCommand.from(aVideo));

        Mockito.when(listVideosUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var aRequest = MockMvcRequestBuilders.get("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .queryParam("cast_members_ids", expectedCastMembers)
                .queryParam("categories_ids", expectedCategories)
                .queryParam("genres_ids", expectedGenres)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aVideo.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].title", Matchers.equalTo(aVideo.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(aVideo.description())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aVideo.createdAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].updated_at", Matchers.equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        Mockito.verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedTerms, actualQuery.terms());
        Assertions.assertEquals(Set.of(CategoryID.from(expectedCategories)), actualQuery.categories());
        Assertions.assertEquals(Set.of(CastMemberID.with(expectedCastMembers)), actualQuery.castMembers());
        Assertions.assertEquals(Set.of(GenreID.from(expectedGenres)), actualQuery.genres());
    }

    @Test
    void givenEmptyParams_whenCallsListVideosWithDefaultValues_shouldReturnPagination() throws Exception {
        final var aVideo = new VideoPreview(Fixture.Videos.video());

        final var expectedPage = 0;
        final var expectedPerPage = 25;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ListVideoOutputCommand.from(aVideo));

        Mockito.when(listVideosUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var aRequest = MockMvcRequestBuilders.get("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aVideo.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].title", Matchers.equalTo(aVideo.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(aVideo.description())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aVideo.createdAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].updated_at", Matchers.equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        Mockito.verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedTerms, actualQuery.terms());
        Assertions.assertTrue(actualQuery.categories().isEmpty());
        Assertions.assertTrue(actualQuery.castMembers().isEmpty());
        Assertions.assertTrue(actualQuery.genres().isEmpty());
    }

    @Test
    void givenAValidVideoIdAndFileType_whenCallsGetMediaById_shouldReturnContent() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedMediaType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedMediaType);
        final var expectedMedia = new FindMediaByIDOutputCommand(expectedResource.getContent(), expectedResource.getContentType(), expectedResource.getName());

        Mockito.when(findMediaByIDUseCase.execute(Mockito.any())).thenReturn(expectedMedia);

        final var aRequest = MockMvcRequestBuilders.get("/videos/{id}/medias/{type}", expectedId.getValue(), expectedMediaType.name())
                .with(ApiTest.VIDEOS_JWT);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, expectedMedia.contentType()))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(expectedMedia.content().length)))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(expectedMedia.name())))
                .andExpect(MockMvcResultMatchers.content().bytes(expectedMedia.content()));

        final var captor = ArgumentCaptor.forClass(FindMediaByIDInputCommand.class);

        Mockito.verify(this.findMediaByIDUseCase).execute(captor.capture());

        final var actualCmd = captor.getValue();
        Assertions.assertEquals(expectedId.getValue(), actualCmd.videoId());
        Assertions.assertEquals(expectedMediaType.name(), actualCmd.mediaType());
    }

    @Test
    void givenAValidVideoIdAndFile_whenCallsUploadMedia_shouldStoreIt() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.getName(), expectedResource.getContentType(), expectedResource.getContent());

        Mockito.when(uploadMediaUseCase.execute(Mockito.any()))
                .thenReturn(new UploadMediaOutputCommand(expectedId.getValue(), expectedType));

        final var aRequest = MockMvcRequestBuilders.multipart("/videos/{id}/medias/{type}", expectedId.getValue(), expectedType.name())
                .file(expectedVideo)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/videos/%s/medias/%s".formatted(expectedId.getValue(), expectedType.name())))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video_id", Matchers.equalTo(expectedId.getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.media_type", Matchers.equalTo(expectedType.name())));

        final var captor = ArgumentCaptor.forClass(UploadMediaInputCommand.class);

        Mockito.verify(this.uploadMediaUseCase).execute(captor.capture());

        final var actualCmd = captor.getValue();
        Assertions.assertEquals(expectedId.getValue(), actualCmd.videoId());
        Assertions.assertEquals(expectedResource.getContent(), actualCmd.videoResource().resource().getContent());
        Assertions.assertEquals(expectedResource.getName(), actualCmd.videoResource().resource().getName());
        Assertions.assertEquals(expectedResource.getContentType(), actualCmd.videoResource().resource().getContentType());
        Assertions.assertEquals(expectedType, actualCmd.videoResource().type());
    }

    @Test
    void givenAnInvalidMediaType_whenCallsUploadMedia_shouldReturnError() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.getName(), expectedResource.getContentType(), expectedResource.getContent());

        // when
        final var aRequest = MockMvcRequestBuilders.multipart("/videos/{id}/medias/INVALID", expectedId.getValue())
                .file(expectedVideo)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Invalid INVALID for VideoMediaType")));
    }
}
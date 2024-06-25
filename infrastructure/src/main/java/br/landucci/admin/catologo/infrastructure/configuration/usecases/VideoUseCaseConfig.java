package br.landucci.admin.catologo.infrastructure.configuration.usecases;

import br.landucci.admin.catologo.application.video.create.CreateVideoUseCase;
import br.landucci.admin.catologo.application.video.create.DefaultCreateVideoUseCase;
import br.landucci.admin.catologo.application.video.delete.DefaultDeleteVideoUseCase;
import br.landucci.admin.catologo.application.video.delete.DeleteVideoUseCase;
import br.landucci.admin.catologo.application.video.find.DefaultFindVideoByIDUseCase;
import br.landucci.admin.catologo.application.video.find.FindVideoByIDUseCase;
import br.landucci.admin.catologo.application.video.list.DefaultListVideosUseCase;
import br.landucci.admin.catologo.application.video.list.ListVideosUseCase;
import br.landucci.admin.catologo.application.video.media.find.DefaultFindMediaByIDUseCase;
import br.landucci.admin.catologo.application.video.media.find.FindMediaByIDUseCase;
import br.landucci.admin.catologo.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import br.landucci.admin.catologo.application.video.media.update.UpdateMediaStatusUseCase;
import br.landucci.admin.catologo.application.video.media.upload.DefaultUploadMediaUseCase;
import br.landucci.admin.catologo.application.video.media.upload.UploadMediaUseCase;
import br.landucci.admin.catologo.application.video.update.DefaultUpdateVideoUseCase;
import br.landucci.admin.catologo.application.video.update.UpdateVideoUseCase;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.video.MediaResourceGateway;
import br.landucci.admin.catologo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final VideoGateway gateway;
    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public VideoUseCaseConfig(final VideoGateway gateway, final CategoryGateway categoryGateway,
                              final CastMemberGateway castMemberGateway, final GenreGateway genreGateway,
                              final MediaResourceGateway mediaResourceGateway
    ) {
        this.gateway = Objects.requireNonNull(gateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(categoryGateway, castMemberGateway, genreGateway, mediaResourceGateway, gateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(gateway, categoryGateway, castMemberGateway, genreGateway, mediaResourceGateway);
    }

    @Bean
    public FindVideoByIDUseCase getVideoByIdUseCase() {
        return new DefaultFindVideoByIDUseCase(gateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(gateway, mediaResourceGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new DefaultListVideosUseCase(gateway);
    }

    @Bean
    public FindMediaByIDUseCase getMediaUseCase() {
        return new DefaultFindMediaByIDUseCase(mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new DefaultUploadMediaUseCase(mediaResourceGateway, gateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(gateway);
    }

}
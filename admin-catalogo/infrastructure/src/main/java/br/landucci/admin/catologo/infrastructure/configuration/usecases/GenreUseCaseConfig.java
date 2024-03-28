package br.landucci.admin.catologo.infrastructure.configuration.usecases;

import br.landucci.admin.catologo.application.genre.create.CreateGenreUseCase;
import br.landucci.admin.catologo.application.genre.create.DefaultCreateGenreUseCase;
import br.landucci.admin.catologo.application.genre.delete.DefaultDeleteGenreUseCase;
import br.landucci.admin.catologo.application.genre.delete.DeleteGenreUseCase;
import br.landucci.admin.catologo.application.genre.find.DefaultFindGenreByIDUseCase;
import br.landucci.admin.catologo.application.genre.find.FindGenreByIDUseCase;
import br.landucci.admin.catologo.application.genre.list.DefaultListGenreUseCase;
import br.landucci.admin.catologo.application.genre.list.ListGenreUseCase;
import br.landucci.admin.catologo.application.genre.update.DefaultUpdateGenreUseCase;
import br.landucci.admin.catologo.application.genre.update.UpdateGenreUseCase;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private GenreGateway gateway;
    private CategoryGateway categoryGateway;

    public GenreUseCaseConfig(final GenreGateway gateway, final CategoryGateway categoryGateway) {
        this.gateway = Objects.requireNonNull(gateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(this.gateway, this.categoryGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(this.gateway, this.categoryGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(this.gateway);
    }

    @Bean
    public FindGenreByIDUseCase findGenreByIDUseCase() {
        return new DefaultFindGenreByIDUseCase(this.gateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(this.gateway);
    }
}

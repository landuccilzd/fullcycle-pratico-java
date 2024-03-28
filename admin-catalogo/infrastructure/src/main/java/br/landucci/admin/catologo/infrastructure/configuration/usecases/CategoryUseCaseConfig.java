package br.landucci.admin.catologo.infrastructure.configuration.usecases;

import br.landucci.admin.catologo.application.category.create.CreateCategoryUseCase;
import br.landucci.admin.catologo.application.category.create.DefaultCreateCategoryUseCase;
import br.landucci.admin.catologo.application.category.delete.DefaultDeleteCategoryUseCase;
import br.landucci.admin.catologo.application.category.delete.DeleteCategoryUseCase;
import br.landucci.admin.catologo.application.category.find.DefaultFindCategoryByIDUseCase;
import br.landucci.admin.catologo.application.category.find.FindCategoryByIDUseCase;
import br.landucci.admin.catologo.application.category.list.DefaultListCategoryUseCase;
import br.landucci.admin.catologo.application.category.list.ListCategoryUseCase;
import br.landucci.admin.catologo.application.category.update.DefaultUpdateCategoryUseCase;
import br.landucci.admin.catologo.application.category.update.UpdateCategoryUseCase;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    public FindCategoryByIDUseCase findByIDCategoryUseCase() {
        return new DefaultFindCategoryByIDUseCase(categoryGateway);
    }

    @Bean
    public ListCategoryUseCase listCategoryUseCase() {
        return new DefaultListCategoryUseCase(categoryGateway);
    }
}

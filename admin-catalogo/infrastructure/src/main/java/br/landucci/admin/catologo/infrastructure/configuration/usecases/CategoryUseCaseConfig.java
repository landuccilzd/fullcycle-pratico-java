package br.landucci.admin.catologo.infrastructure.configuration.usecases;

import br.landucci.admin.catologo.application.category.create.CreateCategoryUseCase;
import br.landucci.admin.catologo.application.category.create.DefaultCreateCategoryUseCase;
import br.landucci.admin.catologo.application.category.delete.DefaultDeleteCategoryUseCase;
import br.landucci.admin.catologo.application.category.delete.DeleteCategoryUseCase;
import br.landucci.admin.catologo.application.category.find.DefaultFindByIDCategoryUseCase;
import br.landucci.admin.catologo.application.category.find.FindByIDCategoryUseCase;
import br.landucci.admin.catologo.application.category.list.DefaultListCategoriesUseCase;
import br.landucci.admin.catologo.application.category.list.ListCategoriesUseCase;
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
    public FindByIDCategoryUseCase findByIDCategoryUseCase() {
        return new DefaultFindByIDCategoryUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoryUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }
}

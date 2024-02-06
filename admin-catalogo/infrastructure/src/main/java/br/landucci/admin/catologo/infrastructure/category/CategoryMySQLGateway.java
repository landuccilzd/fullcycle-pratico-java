package br.landucci.admin.catologo.infrastructure.category;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryJpaEntity;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryRepository;
import br.landucci.admin.catologo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //(ou @Component)
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Category> findById(final CategoryID id) {
        return this.repository.findById(id.getValue()).map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery query) {
        final var page = PageRequest.of(query.page(), query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort()));

        final var specifications = Optional.ofNullable(query.terms()).filter(str -> !str.isBlank())
                .map(str -> {
                    final var nameLike = SpecificationUtils.<CategoryJpaEntity>like("name", str);
                    final var descriptionLike = SpecificationUtils.<CategoryJpaEntity>like("description", str);
                    return nameLike.or(descriptionLike);
                })
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);
        return new Pagination<>(pageResult.getNumber(), pageResult.getSize(), pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).stream().toList()
        );
    }

    @Override
    public Category create(final Category category) {
        return save(category);
    }

    @Override
    public Category update(final Category category) {
        return save(category);
    }

    @Override
    public void deleteById(final CategoryID id) {
        final var idValue = id.getValue();
        if (!this.repository.existsById(idValue)) {
            throw new IllegalArgumentException("Category with ID %s was not found".formatted(id.getValue()));
        }
        this.repository.deleteById(idValue);
    }

    private Category save(Category category) {
        final var entity = CategoryJpaEntity.from(category);
        return this.repository.save(entity).toAggregate();
    }
}

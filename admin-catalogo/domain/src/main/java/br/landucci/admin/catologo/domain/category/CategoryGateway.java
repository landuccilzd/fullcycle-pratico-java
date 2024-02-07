package br.landucci.admin.catologo.domain.category;

import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Optional<Category> findById(CategoryID id);
    Pagination<Category> findAll(SearchQuery query);
    Category create(Category category);
    Category update(Category category);
    void deleteById(CategoryID id);
    List<CategoryID> existsByIds(Iterable<CategoryID> ids);
}

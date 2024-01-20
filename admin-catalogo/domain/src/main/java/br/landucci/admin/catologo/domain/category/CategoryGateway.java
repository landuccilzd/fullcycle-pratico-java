package br.landucci.admin.catologo.domain.category;

import br.landucci.admin.catologo.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Optional<Category> findById(CategoryID id);
    Pagination<Category> findAll(CategorySearchQuery query);
    Category create(Category category);
    Category update(Category category);
    void deleteById(CategoryID id);

}
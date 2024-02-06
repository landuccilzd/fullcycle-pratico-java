package br.landucci.admin.catologo.domain.genre;

import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenraGateway {

    Optional<Genre> findById(GenreID id);
    Pagination<Genre> findAll(SearchQuery query);
    Genre create(Genre genre);
    Genre update(Genre genre);
    void deleteById(GenreID id);
}

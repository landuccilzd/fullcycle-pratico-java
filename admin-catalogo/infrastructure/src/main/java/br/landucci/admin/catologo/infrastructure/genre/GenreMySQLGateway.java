package br.landucci.admin.catologo.infrastructure.genre;

import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {
    @Override
    public Optional<Genre> findById(GenreID id) {
        return Optional.empty();
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery query) {
        return null;
    }

    @Override
    public Genre create(Genre genre) {
        return null;
    }

    @Override
    public Genre update(Genre genre) {
        return null;
    }

    @Override
    public void deleteById(GenreID id) {

    }

}
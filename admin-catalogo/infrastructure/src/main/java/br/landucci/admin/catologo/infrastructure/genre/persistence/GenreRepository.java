package br.landucci.admin.catologo.infrastructure.genre.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreJpaEntity, String> {

    Page<GenreJpaEntity> findAll(Specification<GenreJpaEntity> where, Pageable pageable);

    @Query(value = "select g.id from GenreJpaEntity g where g.id in :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);
}

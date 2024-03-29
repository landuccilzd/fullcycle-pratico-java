package br.landucci.admin.catologo.infrastructure.category.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, String> {

    Page<CategoryJpaEntity> findAll(Specification<CategoryJpaEntity> where, Pageable pageable);

    @Query(value = "select id from CategoryJpaEntity where id in :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);
}

package br.landucci.admin.catologo.infrastructure.castmember.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CastMemberRepository extends JpaRepository<CastMemberJpaEntity, String> {

    Page<CastMemberJpaEntity> findAll(Specification<CastMemberJpaEntity> specification, Pageable page);

    @Query(value = "select castMember.id from CastMemberJpaEntity castMember where castMember.id in :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);
}

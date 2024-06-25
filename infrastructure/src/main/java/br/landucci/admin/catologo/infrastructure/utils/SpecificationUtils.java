package br.landucci.admin.catologo.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() { }

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, qry, cb) -> cb.like(cb.upper(root.get(prop)), "%" + term.toUpperCase() + "%");
    }
}

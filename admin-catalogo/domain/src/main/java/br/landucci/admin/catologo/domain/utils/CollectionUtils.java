package br.landucci.admin.catologo.domain.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <I, O> Set<O> mapTo(final Set<I> list, final Function<I, O> mapper) {
        if (list == null) {
            return null;
        }

        return list.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }

    public static <T> Set<T> nullIfEmpty(final Set<T> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values;
    }
}

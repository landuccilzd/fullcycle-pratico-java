package br.landucci.admin.catologo.domain.exception;

import br.landucci.admin.catologo.domain.AggregateRoot;
import br.landucci.admin.catologo.domain.Identifier;
import br.landucci.admin.catologo.domain.validation.ValidationError;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {
    protected NotFoundException(final String message, final List<ValidationError> errors) {
        super(message, errors);
    }

    public static NotFoundException with(final Class<? extends AggregateRoot<?>> aggregate,
                                         final Identifier id) {
        final var error = "%s with ID %s was not found".formatted(aggregate.getSimpleName(), id.getValue());
        return new NotFoundException(error, Collections.emptyList());
    }
}

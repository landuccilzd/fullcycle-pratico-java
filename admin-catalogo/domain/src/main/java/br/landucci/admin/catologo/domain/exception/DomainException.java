package br.landucci.admin.catologo.domain.exception;

import br.landucci.admin.catologo.domain.validation.ValidationError;

import java.util.List;

public class DomainException extends NoStacktraceException {

    private final transient List<ValidationError> errors;

    protected DomainException(final String message, final List<ValidationError> errors) {
        super(message);
        this.errors = errors;
    }

    public static DomainException with(final ValidationError error) {
        return new DomainException(error.message(), List.of(error));
    }

    public static DomainException with(final List<ValidationError> errors) {
        return new DomainException("", errors);
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
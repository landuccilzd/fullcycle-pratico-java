package br.landucci.admin.catologo.domain.validation.handler;

import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.validation.Validation;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final ValidationError error) {
        throw DomainException.with(error);
    }

    @Override
    public ValidationHandler append(final ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    @Override
    public <T> T validate(final Validation<T> validation) {
        try {
            return validation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(new ValidationError(ex.getMessage()));
        }
    }

    @Override
    public List<ValidationError> getErrors() {
        return List.of();
    }

}
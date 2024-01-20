package br.landucci.admin.catologo.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(ValidationError error);
    ValidationHandler append(ValidationHandler handler);

    ValidationHandler validate(Validation validation);

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default ValidationError firstError() {
        return (hasErrors()) ? getErrors().get(0) : null;
    }

    List<ValidationError> getErrors();
}

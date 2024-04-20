package br.landucci.admin.catologo.domain.genre;

import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;
import br.landucci.admin.catologo.domain.validation.Validator;

public class GenreValidator extends Validator {

    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 255;

    private Genre genre;

    public GenreValidator(final Genre genre, final ValidationHandler handler) {
        super(handler);
        this.genre = genre;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.genre.getName();
        if (name == null) {
            this.validationHandler().append(new ValidationError("Name should not be null"));
            return;
        }
        if (name.isEmpty()) {
            this.validationHandler().append(new ValidationError("Name should not be empty"));
            return;
        }

        final var length = name.trim().length();
        if (length < MIN_NAME_LENGTH || length > MAX_NAME_LENGTH) {
            this.validationHandler().append(new ValidationError("Name must have between 3 and 255 characters"));
        }
    }

}
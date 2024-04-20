package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;
import br.landucci.admin.catologo.domain.validation.Validator;

public class VideoValidator extends Validator {

    private static final int TITLE_MAX_LENGTH = 255;
    private static final int DESCRIPTION_MAX_LENGTH = 4000;

    private final Video video;

    protected VideoValidator(final Video video, final ValidationHandler handler) {
        super(handler);
        this.video = video;
    }

    @Override
    public void validate() {
        checkTitleConstraints();
        checkDescriptionConstraints();
        checkLaunchedAtConstraints();
        checkRatingConstraints();
    }

    private void checkTitleConstraints() {
        final var title = this.video.getTitle();

        if (title == null) {
            this.validationHandler().append(new ValidationError("Title should not be null"));
            return;
        }

        if (title.isBlank()) {
            this.validationHandler().append(new ValidationError("Title should not be empty"));
            return;
        }

        if (title.trim().length() > TITLE_MAX_LENGTH) {
            this.validationHandler().append(new ValidationError("Title must have less then 255 characters"));
        }
    }

    private void checkDescriptionConstraints() {
        final var description = this.video.getDescription();
        if (description == null) {
            this.validationHandler().append(new ValidationError("Description should not be null"));
            return;
        }

        if (description.isBlank()) {
            this.validationHandler().append(new ValidationError("Description should not be empty"));
            return;
        }

        final int length = description.trim().length();
        if (length > DESCRIPTION_MAX_LENGTH) {
            this.validationHandler().append(new ValidationError("Description must have less then 4000 characters"));
        }
    }

    private void checkLaunchedAtConstraints() {
        if (this.video.getLaunchedAt() == null) {
            this.validationHandler().append(new ValidationError("Launched At should not be null"));
        }
    }

    private void checkRatingConstraints() {
        if (this.video.getRating() == null) {
            this.validationHandler().append(new ValidationError("Rating should not be null"));
        }
    }
}

package br.landucci.admin.catologo.domain.validation;

import java.io.Serializable;

public record ValidationError(String message) implements Serializable {

}

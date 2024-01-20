package br.landucci.admin.catologo.application;

import br.landucci.admin.catologo.domain.category.Category;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN in);

}
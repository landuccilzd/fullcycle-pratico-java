package br.landucci.admin.catologo.application;

import br.landucci.admin.catologo.domain.category.Category;

public class UseCase {

    public Category execute() {
        return Category.newCategory("Ficção Científica", "Filmes de ficção científica", true);
    }

}
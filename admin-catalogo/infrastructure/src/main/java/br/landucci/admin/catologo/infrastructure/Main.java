package br.landucci.admin.catologo.infrastructure;

import br.landucci.admin.catologo.application.UseCase;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println(new UseCase().execute());
    }
}
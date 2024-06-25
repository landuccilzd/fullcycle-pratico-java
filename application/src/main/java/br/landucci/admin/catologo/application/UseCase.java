package br.landucci.admin.catologo.application;

public abstract class UseCase<I, O> {
    public abstract O execute(I in);

}
package br.landucci.admin.catologo.application;

public abstract class UnitUseCase<I> {
    public abstract void execute(I in);
}

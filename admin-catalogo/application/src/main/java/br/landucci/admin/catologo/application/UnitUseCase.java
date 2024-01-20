package br.landucci.admin.catologo.application;

public abstract class UnitUseCase<IN> {
    public abstract void execute(IN in);
}

package br.landucci.admin.catologo.domain.category;

public record CategorySearchQuery(int page, int perPage, String terms, String sort, String direction) {}

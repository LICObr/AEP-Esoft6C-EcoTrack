package br.com.ecotrack.domain.model;

// quantidade e por unidade: somar kg com litro nao faz sentido
public record TotalPorMaterial(Material tipo, Unidade unidade, long quantidade, long descartes) {
}

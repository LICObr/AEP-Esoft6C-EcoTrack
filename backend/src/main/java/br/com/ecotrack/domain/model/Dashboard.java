package br.com.ecotrack.domain.model;

import java.util.List;

public record Dashboard(long totalDescartes, List<TotalPorMaterial> porMaterial) {
}

package br.com.ecotrack.dto;

import java.util.List;

public record DashboardResponse(long totalDescartes, List<TotalPorMaterialDto> porMaterial) {
}

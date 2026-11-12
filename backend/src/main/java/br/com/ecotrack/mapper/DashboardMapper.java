package br.com.ecotrack.mapper;

import java.util.List;

import br.com.ecotrack.domain.model.Dashboard;
import br.com.ecotrack.dto.DashboardResponse;
import br.com.ecotrack.dto.TotalPorMaterialDto;

public final class DashboardMapper {

    private DashboardMapper() {
    }

    public static DashboardResponse paraResposta(Dashboard dashboard) {
        List<TotalPorMaterialDto> porMaterial = dashboard.porMaterial().stream()
                .map(total -> new TotalPorMaterialDto(
                        total.tipo(), total.unidade(), total.quantidade(), total.descartes()))
                .toList();

        return new DashboardResponse(dashboard.totalDescartes(), porMaterial);
    }
}

package br.com.ecotrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ecotrack.domain.model.Dashboard;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.TotalPorMaterial;
import br.com.ecotrack.domain.model.Unidade;
import br.com.ecotrack.repository.DescarteRepository;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private DescarteRepository descarteRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void deveMontarODashboardComOTotalEOsTotaisPorMaterial() {
        List<TotalPorMaterial> totais = List.of(
                new TotalPorMaterial(Material.PILHAS, Unidade.UNIDADE, 15, 2));
        when(descarteRepository.count()).thenReturn(2L);
        when(descarteRepository.totalPorMaterial()).thenReturn(totais);

        Dashboard dashboard = dashboardService.montar();

        assertThat(dashboard.totalDescartes()).isEqualTo(2);
        assertThat(dashboard.porMaterial()).isEqualTo(totais);
    }

    @Test
    void deveMontarODashboardVazioQuandoNaoHaDescartes() {
        when(descarteRepository.count()).thenReturn(0L);
        when(descarteRepository.totalPorMaterial()).thenReturn(List.of());

        Dashboard dashboard = dashboardService.montar();

        assertThat(dashboard.totalDescartes()).isZero();
        assertThat(dashboard.porMaterial()).isEmpty();
    }
}

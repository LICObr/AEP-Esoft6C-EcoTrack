package br.com.ecotrack.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.ecotrack.domain.model.Dashboard;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.TotalPorMaterial;
import br.com.ecotrack.domain.model.Unidade;
import br.com.ecotrack.service.DashboardService;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @Test
    void deveDevolverOTotalEOsTotaisPorMaterial() throws Exception {
        when(dashboardService.montar()).thenReturn(new Dashboard(2,
                List.of(new TotalPorMaterial(Material.PILHAS, Unidade.UNIDADE, 15, 2))));

        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDescartes", is(2)))
                .andExpect(jsonPath("$.porMaterial[0].tipo", is("PILHAS")))
                .andExpect(jsonPath("$.porMaterial[0].unidade", is("UNIDADE")))
                .andExpect(jsonPath("$.porMaterial[0].quantidade", is(15)))
                .andExpect(jsonPath("$.porMaterial[0].descartes", is(2)));
    }

    @Test
    void deveDevolverListaVaziaQuandoNaoHaDescartes() throws Exception {
        when(dashboardService.montar()).thenReturn(new Dashboard(0, List.of()));

        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDescartes", is(0)))
                .andExpect(jsonPath("$.porMaterial.length()", is(0)));
    }
}

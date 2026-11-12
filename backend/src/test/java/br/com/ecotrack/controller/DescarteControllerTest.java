package br.com.ecotrack.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.ecotrack.domain.exception.RegraDeNegocioException;
import br.com.ecotrack.domain.model.Descarte;
import br.com.ecotrack.domain.model.ItemDescarte;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.Unidade;
import br.com.ecotrack.service.DescarteService;

@WebMvcTest(DescarteController.class)
class DescarteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DescarteService descarteService;

    @Test
    void deveRegistrarDescarteERetornar201() throws Exception {
        Descarte descarte = new Descarte("u1", "Joao", "p1", "Eco Ponto", List.of(new ItemDescarte(Material.PILHAS, 5, Unidade.UNIDADE)));
        when(descarteService.registrar(any(), any(), any())).thenReturn(descarte);

        mockMvc.perform(post("/api/descartes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuarioId":"u1","pontoColetaId":"p1","materiais":[{"tipo":"PILHAS","quantidade":5,"unidade":"UNIDADE"}]}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuarioId", is("u1")));
    }

    @Test
    void deveRetornar422QuandoMaterialNaoEhAceito() throws Exception {
        when(descarteService.registrar(any(), any(), any()))
                .thenThrow(new RegraDeNegocioException("O ponto de coleta nao aceita o material OLEO."));

        mockMvc.perform(post("/api/descartes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuarioId":"u1","pontoColetaId":"p1","materiais":[{"tipo":"OLEO","quantidade":2,"unidade":"LITRO"}]}
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar400QuandoNaoInformarMateriais() throws Exception {
        mockMvc.perform(post("/api/descartes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuarioId":"u1","pontoColetaId":"p1","materiais":[]}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarHistoricoDoUsuario() throws Exception {
        Descarte descarte = new Descarte("u1", "Joao", "p1", "Eco Ponto", List.of(new ItemDescarte(Material.PILHAS, 5, Unidade.UNIDADE)));
        when(descarteService.listarHistoricoDoUsuario("u1")).thenReturn(List.of(descarte));

        mockMvc.perform(get("/api/descartes/usuario/u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId", is("u1")));
    }
}

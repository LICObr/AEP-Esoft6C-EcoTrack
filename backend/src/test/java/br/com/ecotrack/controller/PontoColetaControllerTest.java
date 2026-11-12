package br.com.ecotrack.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.ecotrack.domain.exception.RecursoNaoEncontradoException;
import br.com.ecotrack.domain.model.Endereco;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.PontoColeta;
import br.com.ecotrack.service.PontoColetaService;

@WebMvcTest(PontoColetaController.class)
class PontoColetaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PontoColetaService pontoColetaService;

    private final Endereco endereco = new Endereco("Av. Brasil", "1200", "Zona 01", "Maringá", "PR");

    @Test
    void deveCadastrarPontoERetornar201() throws Exception {
        when(pontoColetaService.cadastrar(any(), any(), any()))
                .thenReturn(new PontoColeta("Eco Ponto", endereco, Set.of(Material.PILHAS)));

        mockMvc.perform(post("/api/pontos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Eco Ponto","endereco":{"rua":"Av. Brasil","numero":"1","bairro":"Centro","cidade":"Maringá","estado":"PR"},"materiaisAceitos":["PILHAS"]}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Eco Ponto")));
    }

    @Test
    void deveRetornar400QuandoNaoInformarMateriais() throws Exception {
        mockMvc.perform(post("/api/pontos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Eco Ponto","endereco":{"rua":"Av. Brasil","numero":"1","bairro":"Centro","cidade":"Maringá","estado":"PR"},"materiaisAceitos":[]}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarPontosAtivos() throws Exception {
        when(pontoColetaService.filtrar(null, null))
                .thenReturn(List.of(new PontoColeta("Eco Ponto", endereco, Set.of(Material.PILHAS))));

        mockMvc.perform(get("/api/pontos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is("Eco Ponto")));
    }

    @Test
    void deveFiltrarPontosPorMaterial() throws Exception {
        when(pontoColetaService.filtrar(Material.VIDRO, null))
                .thenReturn(List.of(new PontoColeta("Eco Vidro", endereco, Set.of(Material.VIDRO))));

        mockMvc.perform(get("/api/pontos").param("material", "VIDRO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is("Eco Vidro")));
    }

    @Test
    void deveFiltrarPontosPorCidade() throws Exception {
        when(pontoColetaService.filtrar(null, "Sarandi"))
                .thenReturn(List.of(new PontoColeta("Eco Sarandi", endereco, Set.of(Material.PILHAS))));

        mockMvc.perform(get("/api/pontos").param("cidade", "Sarandi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is("Eco Sarandi")));
    }

    @Test
    void deveFiltrarPontosPorMaterialECidade() throws Exception {
        when(pontoColetaService.filtrar(Material.VIDRO, "Maringa"))
                .thenReturn(List.of(new PontoColeta("Eco Vidro", endereco, Set.of(Material.VIDRO))));

        mockMvc.perform(get("/api/pontos").param("material", "VIDRO").param("cidade", "Maringa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is("Eco Vidro")));
    }

    @Test
    void deveListarAsCidadesComPontoAtivo() throws Exception {
        when(pontoColetaService.listarCidades()).thenReturn(List.of("Maringa", "Sarandi"));

        mockMvc.perform(get("/api/pontos/cidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is("Maringa")))
                .andExpect(jsonPath("$[1]", is("Sarandi")));
    }

    @Test
    void deveRetornar400QuandoMaterialForInvalido() throws Exception {
        mockMvc.perform(get("/api/pontos").param("material", "NAO_EXISTE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404QuandoPontoNaoExiste() throws Exception {
        when(pontoColetaService.buscarPorId("1"))
                .thenThrow(new RecursoNaoEncontradoException("Ponto de coleta nao encontrado."));

        mockMvc.perform(get("/api/pontos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDesativarPontoERetornar204() throws Exception {
        mockMvc.perform(delete("/api/pontos/1"))
                .andExpect(status().isNoContent());
    }
}

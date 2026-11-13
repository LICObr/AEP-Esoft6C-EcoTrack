package br.com.ecotrack.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
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

import br.com.ecotrack.domain.exception.ConflitoException;
import br.com.ecotrack.domain.exception.RecursoNaoEncontradoException;
import br.com.ecotrack.domain.model.Usuario;
import br.com.ecotrack.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void deveCadastrarUsuarioERetornar201() throws Exception {
        when(usuarioService.cadastrar("Joao", "joao@email.com"))
                .thenReturn(new Usuario("Joao", "joao@email.com"));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Joao\",\"email\":\"joao@email.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("joao@email.com")));
    }

    @Test
    void deveRetornar400QuandoEmailForInvalido() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Joao\",\"email\":\"invalido\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar409QuandoEmailJaExiste() throws Exception {
        when(usuarioService.cadastrar(eq("Joao"), eq("joao@email.com")))
                .thenThrow(new ConflitoException("Ja existe um usuario cadastrado com este email."));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Joao\",\"email\":\"joao@email.com\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void deveRetornar404QuandoUsuarioNaoExiste() throws Exception {
        when(usuarioService.buscarPorId("1"))
                .thenThrow(new RecursoNaoEncontradoException("Usuario nao encontrado."));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarUsuarios() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of(new Usuario("Joao", "joao@email.com")));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is("Joao")));
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        when(usuarioService.buscarPorId("1")).thenReturn(new Usuario("Joao", "joao@email.com"));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Joao")))
                .andExpect(jsonPath("$.email", is("joao@email.com")));
    }
}

package br.com.ecotrack.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void deveCriarUsuarioComDadosValidos() {
        Usuario usuario = new Usuario("Pedro", "pedro@email.com");

        assertThat(usuario.getNome()).isEqualTo("Pedro");
        assertThat(usuario.getEmail()).isEqualTo("pedro@email.com");
    }

    @Test
    void deveRejeitarCriacaoQuandoNomeEstiverEmBranco() {
        assertThrows(IllegalArgumentException.class, () -> new Usuario(" ", "pedro@email.com"));
    }

    @Test
    void deveRejeitarCriacaoQuandoEmailEstiverEmBranco() {
        assertThrows(IllegalArgumentException.class, () -> new Usuario("Pedro", ""));
    }
}
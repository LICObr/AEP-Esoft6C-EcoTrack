package br.com.ecotrack.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EnderecoTest {

    @Test
    void deveCriarEnderecoComDadosValidos() {
        Endereco endereco = new Endereco("Av. Brasil", "1200", "Zona 01", "Maringá", "PR");

        assertThat(endereco.rua()).isEqualTo("Av. Brasil");
        assertThat(endereco.cidade()).isEqualTo("Maringá");
    }

    @Test
    void deveRejeitarEnderecoSemRua() {
        assertThrows(IllegalArgumentException.class,
                () -> new Endereco(" ", "1200", "Zona 01", "Maringá", "PR"));
    }

    @Test
    void deveRejeitarEnderecoSemCidade() {
        assertThrows(IllegalArgumentException.class,
                () -> new Endereco("Av. Brasil", "1200", "Zona 01", null, "PR"));
    }

    @Test
    void deveRejeitarEnderecoSemEstado() {
        assertThrows(IllegalArgumentException.class,
                () -> new Endereco("Av. Brasil", "1200", "Zona 01", "Maringá", ""));
    }
}

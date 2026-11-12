package br.com.ecotrack.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import br.com.ecotrack.domain.model.Usuario;

class UsuarioRepositoryIntegracaoTest extends MongoIntegracaoBase {

    @Autowired
    private UsuarioRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void deveEncontrarPeloEmail() {
        repository.save(new Usuario("Joao", "joao@email.com"));

        assertThat(repository.existsByEmail("joao@email.com")).isTrue();
        assertThat(repository.findByEmail("joao@email.com")).get()
                .extracting(Usuario::getNome).isEqualTo("Joao");
    }

    @Test
    void deveRecusarEmailRepetidoPeloIndiceUnico() {
        repository.save(new Usuario("Joao", "joao@email.com"));

        assertThrows(DuplicateKeyException.class,
                () -> repository.save(new Usuario("Outro Joao", "joao@email.com")));
    }
}

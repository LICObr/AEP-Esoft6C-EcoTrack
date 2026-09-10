package br.com.ecotrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ecotrack.domain.exception.ConflitoException;
import br.com.ecotrack.domain.exception.RecursoNaoEncontradoException;
import br.com.ecotrack.domain.model.Usuario;
import br.com.ecotrack.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCadastrarUsuarioComEmailNovo() {
        when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(chamada -> chamada.getArgument(0));

        Usuario usuario = usuarioService.cadastrar("Joao", "joao@email.com");

        assertThat(usuario.getNome()).isEqualTo("Joao");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveRejeitarCadastroComEmailDuplicado() {
        when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(true);

        assertThrows(ConflitoException.class, () -> usuarioService.cadastrar("Joao", "joao@email.com"));
    }

    @Test
    void deveBuscarUsuarioPorId() {
        Usuario usuario = new Usuario("Joao", "joao@email.com");
        when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));

        Usuario encontrado = usuarioService.buscarPorId("1");

        assertThat(encontrado.getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> usuarioService.buscarPorId("1"));
    }
}

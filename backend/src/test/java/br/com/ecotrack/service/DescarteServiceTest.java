package br.com.ecotrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.ecotrack.domain.exception.RecursoNaoEncontradoException;
import br.com.ecotrack.domain.exception.RegraDeNegocioException;
import br.com.ecotrack.domain.model.Descarte;
import br.com.ecotrack.domain.model.Endereco;
import br.com.ecotrack.domain.model.ItemDescarte;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.PontoColeta;
import br.com.ecotrack.domain.model.Unidade;
import br.com.ecotrack.domain.model.Usuario;
import br.com.ecotrack.repository.DescarteRepository;

@ExtendWith(MockitoExtension.class)
class DescarteServiceTest {

    @Mock
    private DescarteRepository descarteRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PontoColetaService pontoColetaService;

    @InjectMocks
    private DescarteService descarteService;

    private final Endereco endereco = new Endereco("Av. Brasil", "1200", "Zona 01", "Maringá", "PR");

    private Usuario usuarioComId(String id) {
        Usuario usuario = new Usuario("Joao", "joao@email.com");
        ReflectionTestUtils.setField(usuario, "id", id);
        return usuario;
    }

    private PontoColeta pontoComId(String id, Material... materiais) {
        PontoColeta ponto = new PontoColeta("Eco Ponto", endereco, Set.of(materiais));
        ReflectionTestUtils.setField(ponto, "id", id);
        return ponto;
    }

    @Test
    void deveRegistrarDescarteQuandoPontoAceitaOMaterial() {
        Usuario usuario = usuarioComId("u1");
        PontoColeta ponto = pontoComId("p1", Material.PILHAS);
        List<ItemDescarte> materiais = List.of(new ItemDescarte(Material.PILHAS, 5, Unidade.UNIDADE));

        when(usuarioService.buscarPorId("u1")).thenReturn(usuario);
        when(pontoColetaService.buscarPorId("p1")).thenReturn(ponto);
        when(descarteRepository.save(any(Descarte.class))).thenAnswer(c -> c.getArgument(0));

        Descarte descarte = descarteService.registrar("u1", "p1", materiais);

        assertThat(descarte.totalItens()).isEqualTo(5);
    }

    @Test
    void deveRejeitarQuandoUsuarioNaoExiste() {
        when(usuarioService.buscarPorId("u1")).thenThrow(new RecursoNaoEncontradoException("Usuario nao encontrado."));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> descarteService.registrar("u1", "p1", List.of(new ItemDescarte(Material.PILHAS, 1, Unidade.UNIDADE))));
    }

    @Test
    void deveRejeitarQuandoPontoNaoExiste() {
        Usuario usuario = usuarioComId("u1");
        when(usuarioService.buscarPorId("u1")).thenReturn(usuario);
        when(pontoColetaService.buscarPorId("p1")).thenThrow(new RecursoNaoEncontradoException("Ponto nao encontrado."));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> descarteService.registrar("u1", "p1", List.of(new ItemDescarte(Material.PILHAS, 1, Unidade.UNIDADE))));
    }

    @Test
    void deveRejeitarQuandoPontoEstaInativo() {
        Usuario usuario = usuarioComId("u1");
        PontoColeta ponto = pontoComId("p1", Material.PILHAS);
        ponto.desativar();

        when(usuarioService.buscarPorId("u1")).thenReturn(usuario);
        when(pontoColetaService.buscarPorId("p1")).thenReturn(ponto);

        assertThrows(RegraDeNegocioException.class,
                () -> descarteService.registrar("u1", "p1", List.of(new ItemDescarte(Material.PILHAS, 1, Unidade.UNIDADE))));
    }

    @Test
    void deveRejeitarQuandoMaterialNaoEhAceitoPeloPonto() {
        Usuario usuario = usuarioComId("u1");
        PontoColeta ponto = pontoComId("p1", Material.PILHAS);

        when(usuarioService.buscarPorId("u1")).thenReturn(usuario);
        when(pontoColetaService.buscarPorId("p1")).thenReturn(ponto);

        assertThrows(RegraDeNegocioException.class,
                () -> descarteService.registrar("u1", "p1", List.of(new ItemDescarte(Material.OLEO, 1, Unidade.LITRO))));
    }

    @Test
    void deveListarHistoricoOrdenadoDoUsuario() {
        Usuario usuario = usuarioComId("u1");
        when(usuarioService.buscarPorId("u1")).thenReturn(usuario);
        when(descarteRepository.findByUsuarioIdOrderByDataDesc("u1")).thenReturn(List.of());

        List<Descarte> historico = descarteService.listarHistoricoDoUsuario("u1");

        assertThat(historico).isEmpty();
    }
}

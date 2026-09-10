package br.com.ecotrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ecotrack.domain.exception.RecursoNaoEncontradoException;
import br.com.ecotrack.domain.model.Endereco;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.PontoColeta;
import br.com.ecotrack.repository.PontoColetaRepository;

@ExtendWith(MockitoExtension.class)
class PontoColetaServiceTest {

    @Mock
    private PontoColetaRepository pontoColetaRepository;

    @InjectMocks
    private PontoColetaService pontoColetaService;

    private final Endereco endereco = new Endereco("Av. Brasil", "1200", "Zona 01", "Maringá", "PR");

    @Test
    void deveCadastrarPontoColeta() {
        when(pontoColetaRepository.save(any(PontoColeta.class))).thenAnswer(c -> c.getArgument(0));

        PontoColeta ponto = pontoColetaService.cadastrar("Eco Ponto", endereco, Set.of(Material.PILHAS));

        assertThat(ponto.getNome()).isEqualTo("Eco Ponto");
    }

    @Test
    void deveRejeitarCadastroSemMateriais() {
        assertThrows(IllegalArgumentException.class,
                () -> pontoColetaService.cadastrar("Eco Ponto", endereco, Set.of()));
    }

    @Test
    void deveFiltrarPontosPorMaterial() {
        PontoColeta ponto = new PontoColeta("Eco Ponto", endereco, Set.of(Material.VIDRO));
        when(pontoColetaRepository.findByAtivoTrueAndMateriaisAceitosContaining(Material.VIDRO))
                .thenReturn(List.of(ponto));

        List<PontoColeta> resultado = pontoColetaService.filtrarPorMaterial(Material.VIDRO);

        assertThat(resultado).hasSize(1);
    }

    @Test
    void deveAtualizarPontoExistente() {
        PontoColeta ponto = new PontoColeta("Eco Ponto", endereco, Set.of(Material.PILHAS));
        when(pontoColetaRepository.findById("1")).thenReturn(Optional.of(ponto));
        when(pontoColetaRepository.save(any(PontoColeta.class))).thenAnswer(c -> c.getArgument(0));

        PontoColeta atualizado = pontoColetaService.atualizar("1", "Novo Nome", endereco, Set.of(Material.VIDRO));

        assertThat(atualizado.getNome()).isEqualTo("Novo Nome");
    }

    @Test
    void deveDesativarPontoExistente() {
        PontoColeta ponto = new PontoColeta("Eco Ponto", endereco, Set.of(Material.PILHAS));
        when(pontoColetaRepository.findById("1")).thenReturn(Optional.of(ponto));

        pontoColetaService.desativar("1");

        assertThat(ponto.isAtivo()).isFalse();
    }

    @Test
    void deveLancarExcecaoQuandoPontoNaoExiste() {
        when(pontoColetaRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> pontoColetaService.buscarPorId("1"));
    }
}

package br.com.ecotrack.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

class PontoColetaTest {

    private final Endereco enderecoValido =
            new Endereco("Av. Brasil", "1200", "Zona 01", "Maringá", "PR");

    @Test
    void deveCriarPontoColetaAtivoComDadosValidos() {
        PontoColeta ponto = new PontoColeta("Eco Ponto Centro", enderecoValido, Set.of(Material.PILHAS));

        assertThat(ponto.isAtivo()).isTrue();
        assertThat(ponto.getMateriaisAceitos()).containsExactly(Material.PILHAS);
    }

    @Test
    void deveRejeitarCriacaoSemNome() {
        assertThrows(IllegalArgumentException.class,
                () -> new PontoColeta(" ", enderecoValido, Set.of(Material.PILHAS)));
    }

    @Test
    void deveRejeitarCriacaoSemEndereco() {
        assertThrows(IllegalArgumentException.class,
                () -> new PontoColeta("Eco Ponto Centro", null, Set.of(Material.PILHAS)));
    }

    @Test
    void deveRejeitarCriacaoSemMateriais() {
        assertThrows(IllegalArgumentException.class,
                () -> new PontoColeta("Eco Ponto Centro", enderecoValido, Set.of()));
    }

    @Test
    void deveAceitarMaterialQueEstaNaLista() {
        PontoColeta ponto = new PontoColeta("Eco Ponto Centro", enderecoValido, Set.of(Material.PILHAS));

        assertThat(ponto.aceita(Material.PILHAS)).isTrue();
    }

    @Test
    void deveRecusarMaterialQueNaoEstaNaLista() {
        PontoColeta ponto = new PontoColeta("Eco Ponto Centro", enderecoValido, Set.of(Material.PILHAS));

        assertThat(ponto.aceita(Material.OLEO)).isFalse();
    }

    @Test
    void deveDesativarPontoColeta() {
        PontoColeta ponto = new PontoColeta("Eco Ponto Centro", enderecoValido, Set.of(Material.PILHAS));

        ponto.desativar();

        assertThat(ponto.isAtivo()).isFalse();
    }

    @Test
    void deveAtualizarDadosDoPonto() {
        PontoColeta ponto = new PontoColeta("Eco Ponto Centro", enderecoValido, Set.of(Material.PILHAS));

        ponto.atualizar("Eco Ponto Zona 7", enderecoValido, Set.of(Material.VIDRO, Material.METAL));

        assertThat(ponto.getNome()).isEqualTo("Eco Ponto Zona 7");
        assertThat(ponto.getMateriaisAceitos()).containsExactlyInAnyOrder(Material.VIDRO, Material.METAL);
    }

    @Test
    void deveRejeitarAtualizacaoSemMateriais() {
        PontoColeta ponto = new PontoColeta("Eco Ponto Centro", enderecoValido, Set.of(Material.PILHAS));

        assertThrows(IllegalArgumentException.class,
                () -> ponto.atualizar("Eco Ponto Zona 7", enderecoValido, Set.of()));
    }
}

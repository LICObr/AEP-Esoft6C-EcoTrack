package br.com.ecotrack.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import br.com.ecotrack.domain.exception.RegraDeNegocioException;

class DescarteTest {

    private final Endereco endereco = new Endereco("Av. Brasil", "1200", "Zona 01", "Maringá", "PR");

    @Test
    void deveCalcularTotalDeItens() {
        Descarte descarte = new Descarte("u1", "Joao", "p1", "Eco Ponto", List.of(
                new ItemDescarte(Material.PILHAS, 5, Unidade.UNIDADE),
                new ItemDescarte(Material.VIDRO, 3, Unidade.KG)));

        assertThat(descarte.totalItens()).isEqualTo(8);
    }

    @Test
    void deveRejeitarCriacaoSemUsuario() {
        assertThrows(IllegalArgumentException.class,
                () -> new Descarte(" ", "Joao", "p1", "Eco Ponto", List.of(new ItemDescarte(Material.PILHAS, 1, Unidade.UNIDADE))));
    }

    @Test
    void deveRejeitarCriacaoSemPontoColeta() {
        assertThrows(IllegalArgumentException.class,
                () -> new Descarte("u1", "Joao", " ", "Eco Ponto", List.of(new ItemDescarte(Material.PILHAS, 1, Unidade.UNIDADE))));
    }

    @Test
    void deveRejeitarCriacaoSemNomeDoUsuario() {
        assertThrows(IllegalArgumentException.class,
                () -> new Descarte("u1", " ", "p1", "Eco Ponto", List.of(new ItemDescarte(Material.PILHAS, 1, Unidade.UNIDADE))));
    }

    @Test
    void deveRejeitarCriacaoSemNomeDoPonto() {
        assertThrows(IllegalArgumentException.class,
                () -> new Descarte("u1", "Joao", "p1", null, List.of(new ItemDescarte(Material.PILHAS, 1, Unidade.UNIDADE))));
    }

    @Test
    void deveGuardarOsNomesInformados() {
        Descarte descarte = new Descarte("u1", "Joao", "p1", "Eco Ponto",
                List.of(new ItemDescarte(Material.PILHAS, 1, Unidade.UNIDADE)));

        assertThat(descarte.getUsuarioNome()).isEqualTo("Joao");
        assertThat(descarte.getPontoColetaNome()).isEqualTo("Eco Ponto");
    }

    @Test
    void deveRejeitarCriacaoSemMateriais() {
        assertThrows(IllegalArgumentException.class, () -> new Descarte("u1", "Joao", "p1", "Eco Ponto", List.of()));
    }

    @Test
    void deveValidarComSucessoQuandoPontoAceitaTodosOsMateriais() {
        PontoColeta ponto = new PontoColeta("Eco Ponto", endereco, Set.of(Material.PILHAS));
        Descarte descarte = new Descarte("u1", "Joao", "p1", "Eco Ponto",
                List.of(new ItemDescarte(Material.PILHAS, 5, Unidade.UNIDADE)));

        descarte.validarContra(ponto);
    }

    @Test
    void deveRejeitarQuandoPontoEstaInativo() {
        PontoColeta ponto = new PontoColeta("Eco Ponto", endereco, Set.of(Material.PILHAS));
        ponto.desativar();
        Descarte descarte = new Descarte("u1", "Joao", "p1", "Eco Ponto",
                List.of(new ItemDescarte(Material.PILHAS, 5, Unidade.UNIDADE)));

        assertThrows(RegraDeNegocioException.class, () -> descarte.validarContra(ponto));
    }

    @Test
    void deveRejeitarQuandoPontoNaoAceitaOMaterial() {
        PontoColeta ponto = new PontoColeta("Eco Ponto", endereco, Set.of(Material.PILHAS));
        Descarte descarte = new Descarte("u1", "Joao", "p1", "Eco Ponto",
                List.of(new ItemDescarte(Material.OLEO, 2, Unidade.LITRO)));

        assertThrows(RegraDeNegocioException.class, () -> descarte.validarContra(ponto));
    }
}

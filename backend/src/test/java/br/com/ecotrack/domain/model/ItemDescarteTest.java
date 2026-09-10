package br.com.ecotrack.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ItemDescarteTest {

    @Test
    void deveCriarItemDescarteComDadosValidos() {
        ItemDescarte item = new ItemDescarte(Material.PILHAS, 3, Unidade.UNIDADE);

        assertThat(item.tipo()).isEqualTo(Material.PILHAS);
        assertThat(item.quantidade()).isEqualTo(3);
        assertThat(item.unidade()).isEqualTo(Unidade.UNIDADE);
    }

    @Test
    void deveRejeitarItemSemTipo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemDescarte(null, 3, Unidade.UNIDADE));
    }

    @Test
    void deveRejeitarItemComQuantidadeZero() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemDescarte(Material.PILHAS, 0, Unidade.UNIDADE));
    }

    @Test
    void deveRejeitarItemComQuantidadeNegativa() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemDescarte(Material.PILHAS, -1, Unidade.UNIDADE));
    }

    @Test
    void deveRejeitarItemSemUnidade() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemDescarte(Material.PILHAS, 3, null));
    }
}

package br.com.ecotrack.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaterialTest {

    @Test
    void deveConterOsSeteMateriaisAceitosPelaPlataforma() {
        assertThat(Material.values()).containsExactlyInAnyOrder(
                Material.PILHAS, Material.ELETRONICOS, Material.OLEO,
                Material.PLASTICO, Material.PAPEL, Material.VIDRO, Material.METAL);
    }
}

package br.com.ecotrack.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.com.ecotrack.config.MongoConfig;
import br.com.ecotrack.domain.model.Descarte;
import br.com.ecotrack.domain.model.ItemDescarte;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.TotalPorMaterial;
import br.com.ecotrack.domain.model.Unidade;

// precisa do docker rodando
@DataMongoTest
@Testcontainers
@Import(MongoConfig.class)
class DescarteRepositoryIntegracaoTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7");

    @Autowired
    private DescarteRepository descarteRepository;

    @BeforeEach
    void limparBanco() {
        descarteRepository.deleteAll();
    }

    private Descarte descarte(String usuarioId, ItemDescarte... itens) {
        return descarteRepository.save(
                new Descarte(usuarioId, "Usuario " + usuarioId, "p1", "Eco Ponto", List.of(itens)));
    }

    @Test
    void deveSomarAQuantidadePorMaterialSeparandoPorUnidade() {
        descarte("u1", new ItemDescarte(Material.PILHAS, 10, Unidade.UNIDADE));
        descarte("u2", new ItemDescarte(Material.PILHAS, 5, Unidade.UNIDADE),
                new ItemDescarte(Material.PAPEL, 3, Unidade.KG));
        descarte("u1", new ItemDescarte(Material.PAPEL, 2, Unidade.UNIDADE));

        List<TotalPorMaterial> totais = descarteRepository.totalPorMaterial();

        assertThat(totais).containsExactly(
                new TotalPorMaterial(Material.PAPEL, Unidade.KG, 3, 1),
                new TotalPorMaterial(Material.PAPEL, Unidade.UNIDADE, 2, 1),
                new TotalPorMaterial(Material.PILHAS, Unidade.UNIDADE, 15, 2));
    }

    @Test
    void deveDevolverListaVaziaQuandoNaoHaDescartes() {
        assertThat(descarteRepository.totalPorMaterial()).isEmpty();
    }

    @Test
    void deveListarOHistoricoDoUsuarioDoMaisRecentePraMaisAntigo() throws InterruptedException {
        Descarte primeiro = descarte("u1", new ItemDescarte(Material.PILHAS, 1, Unidade.UNIDADE));
        Thread.sleep(5);
        Descarte segundo = descarte("u1", new ItemDescarte(Material.VIDRO, 1, Unidade.KG));
        descarte("u2", new ItemDescarte(Material.METAL, 1, Unidade.KG));

        List<Descarte> historico = descarteRepository.findByUsuarioIdOrderByDataDesc("u1");

        assertThat(historico).extracting(Descarte::getId)
                .containsExactly(segundo.getId(), primeiro.getId());
    }
}

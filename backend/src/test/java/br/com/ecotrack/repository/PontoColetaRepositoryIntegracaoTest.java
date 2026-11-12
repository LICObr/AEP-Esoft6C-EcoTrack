package br.com.ecotrack.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.ecotrack.domain.model.Endereco;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.PontoColeta;

class PontoColetaRepositoryIntegracaoTest extends MongoIntegracaoBase {

    @Autowired
    private PontoColetaRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    private PontoColeta salvar(String nome, String cidade, Material... materiais) {
        Endereco endereco = new Endereco("Rua A", "10", "Centro", cidade, "PR");
        return repository.save(new PontoColeta(nome, endereco, Set.of(materiais)));
    }

    @Test
    void deveFiltrarPorMaterialSoEntreOsAtivos() {
        salvar("Ativo com pilhas", "Maringa", Material.PILHAS);
        salvar("Ativo com vidro", "Maringa", Material.VIDRO);
        PontoColeta inativo = salvar("Inativo com pilhas", "Maringa", Material.PILHAS);
        inativo.desativar();
        repository.save(inativo);

        List<PontoColeta> pontos = repository.findByAtivoTrueAndMateriaisAceitosContaining(Material.PILHAS);

        assertThat(pontos).extracting(PontoColeta::getNome).containsExactly("Ativo com pilhas");
    }

    @Test
    void deveFiltrarPorCidade() {
        salvar("Maringa 1", "Maringa", Material.PILHAS);
        salvar("Sarandi 1", "Sarandi", Material.PILHAS);

        List<PontoColeta> pontos = repository.findByAtivoTrueAndEnderecoCidade("Sarandi");

        assertThat(pontos).extracting(PontoColeta::getNome).containsExactly("Sarandi 1");
    }

    @Test
    void deveFiltrarPorMaterialECidadeJuntos() {
        salvar("Maringa pilhas", "Maringa", Material.PILHAS);
        salvar("Maringa vidro", "Maringa", Material.VIDRO);
        salvar("Sarandi pilhas", "Sarandi", Material.PILHAS);

        List<PontoColeta> pontos = repository
                .findByAtivoTrueAndMateriaisAceitosContainingAndEnderecoCidade(Material.PILHAS, "Maringa");

        assertThat(pontos).extracting(PontoColeta::getNome).containsExactly("Maringa pilhas");
    }

    @Test
    void deveListarAsCidadesSemRepetirEIgnorandoPontosInativos() {
        salvar("Maringa 1", "Maringa", Material.PILHAS);
        salvar("Maringa 2", "Maringa", Material.VIDRO);
        salvar("Sarandi 1", "Sarandi", Material.PILHAS);
        PontoColeta inativo = salvar("Paicandu 1", "Paicandu", Material.PILHAS);
        inativo.desativar();
        repository.save(inativo);

        assertThat(repository.listarCidadesAtivas()).containsExactly("Maringa", "Sarandi");
    }
}

package br.com.ecotrack.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexInfo;

import br.com.ecotrack.domain.model.Descarte;
import br.com.ecotrack.domain.model.PontoColeta;
import br.com.ecotrack.domain.model.Usuario;

class IndicesMongoIntegracaoTest extends MongoIntegracaoBase {

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<String> nomesDosIndices(Class<?> entidade) {
        return mongoTemplate.indexOps(entidade).getIndexInfo().stream().map(IndexInfo::getName).toList();
    }

    @Test
    void deveCriarOsIndicesDeclaradosNasEntidades() {
        assertThat(nomesDosIndices(Usuario.class)).contains("email");
        assertThat(nomesDosIndices(PontoColeta.class)).contains("ativo_materiais", "ativo_cidade");
        assertThat(nomesDosIndices(Descarte.class)).contains("usuario_data");
    }

    @Test
    void deveAtenderOHistoricoComOIndiceSemOrdenarEmMemoria() {
        Document comando = new Document("explain", new Document("find", "descartes")
                .append("filter", new Document("usuarioId", "u1"))
                .append("sort", new Document("data", -1)))
                .append("verbosity", "queryPlanner");

        String planoVencedor = mongoTemplate.getDb().runCommand(comando)
                .get("queryPlanner", Document.class)
                .get("winningPlan", Document.class)
                .toJson();

        assertThat(planoVencedor).contains("IXSCAN").contains("usuario_data");
        assertThat(planoVencedor).doesNotContain("\"stage\": \"SORT\"");
    }
}

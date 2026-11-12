package br.com.ecotrack.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoWriteException;

import br.com.ecotrack.config.EsquemaMongo;
import br.com.ecotrack.domain.model.Descarte;
import br.com.ecotrack.domain.model.ItemDescarte;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.Unidade;
import br.com.ecotrack.domain.model.Usuario;

class EsquemaMongoIntegracaoTest extends MongoIntegracaoBase {

    // codigo 121 do mongo = document failed validation
    private static final int FALHA_DE_VALIDACAO = 121;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DescarteRepository descarteRepository;

    @BeforeEach
    void aplicarEsquemasELimpar() {
        EsquemaMongo.aplicar(mongoTemplate);
        usuarioRepository.deleteAll();
        descarteRepository.deleteAll();
        mongoTemplate.getCollection("pontos_coleta").deleteMany(new Document());
    }

    private void inserir(String colecao, Document documento) {
        mongoTemplate.getCollection(colecao).insertOne(documento);
    }

    private void assertRecusado(String colecao, Document documento) {
        MongoWriteException erro = assertThrows(MongoWriteException.class, () -> inserir(colecao, documento));
        assertThat(erro.getError().getCode()).isEqualTo(FALHA_DE_VALIDACAO);
    }

    @Test
    void deveAceitarOsDocumentosQueAAplicacaoGrava() {
        usuarioRepository.save(new Usuario("Joao", "joao@email.com"));
        descarteRepository.save(new Descarte("u1", "Joao", "p1", "Eco Ponto",
                List.of(new ItemDescarte(Material.PILHAS, 2, Unidade.UNIDADE))));

        assertThat(usuarioRepository.count()).isEqualTo(1);
        assertThat(descarteRepository.count()).isEqualTo(1);
    }

    @Test
    void deveRecusarUsuarioComEmailInvalido() {
        assertRecusado("usuarios", new Document("nome", "Joao").append("email", "invalido"));
    }

    @Test
    void deveRecusarPontoSemNenhumMaterial() {
        assertRecusado("pontos_coleta", new Document("nome", "Eco Ponto")
                .append("ativo", true)
                .append("endereco", new Document("rua", "Rua A").append("cidade", "Maringa").append("estado", "PR"))
                .append("materiaisAceitos", List.of()));
    }

    @Test
    void deveRecusarPontoComMaterialQueNaoExiste() {
        assertRecusado("pontos_coleta", new Document("nome", "Eco Ponto")
                .append("ativo", true)
                .append("endereco", new Document("rua", "Rua A").append("cidade", "Maringa").append("estado", "PR"))
                .append("materiaisAceitos", List.of("PLUTONIO")));
    }

    @Test
    void deveRecusarDescarteComQuantidadeZero() {
        assertRecusado("descartes", new Document("usuarioId", "u1")
                .append("usuarioNome", "Joao")
                .append("pontoColetaId", "p1")
                .append("pontoColetaNome", "Eco Ponto")
                .append("materiais", List.of(
                        new Document("tipo", "PILHAS").append("quantidade", 0).append("unidade", "UNIDADE"))));
    }
}

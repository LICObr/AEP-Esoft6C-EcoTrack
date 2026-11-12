package br.com.ecotrack.config;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.Unidade;

// regras de formato que o proprio mongo confere, mesmo se alguem gravar fora do java
public final class EsquemaMongo {

    private EsquemaMongo() {
    }

    public static Document usuarios() {
        return objeto(List.of("nome", "email"), new Document()
                .append("nome", texto())
                .append("email", new Document("bsonType", "string")
                        .append("pattern", "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")));
    }

    public static Document pontosColeta() {
        Document endereco = objeto(List.of("rua", "cidade", "estado"), new Document()
                .append("rua", texto())
                .append("cidade", texto())
                .append("estado", texto()));

        return objeto(List.of("nome", "endereco", "materiaisAceitos", "ativo"), new Document()
                .append("nome", texto())
                .append("endereco", endereco)
                .append("materiaisAceitos", new Document("bsonType", "array")
                        .append("minItems", 1)
                        .append("uniqueItems", true)
                        .append("items", enumerado(nomes(Material.values()))))
                .append("ativo", new Document("bsonType", "bool")));
    }

    public static Document descartes() {
        Document item = objeto(List.of("tipo", "quantidade", "unidade"), new Document()
                .append("tipo", enumerado(nomes(Material.values())))
                .append("quantidade", new Document("bsonType", "int").append("minimum", 1))
                .append("unidade", enumerado(nomes(Unidade.values()))));

        return objeto(List.of("usuarioId", "usuarioNome", "pontoColetaId", "pontoColetaNome", "materiais"),
                new Document()
                        .append("usuarioId", texto())
                        .append("usuarioNome", texto())
                        .append("pontoColetaId", texto())
                        .append("pontoColetaNome", texto())
                        .append("materiais", new Document("bsonType", "array")
                                .append("minItems", 1)
                                .append("items", item)));
    }

    public static void aplicar(MongoTemplate mongoTemplate) {
        aplicarEm(mongoTemplate, "usuarios", usuarios());
        aplicarEm(mongoTemplate, "pontos_coleta", pontosColeta());
        aplicarEm(mongoTemplate, "descartes", descartes());
    }

    // moderate: documentos antigos fora do padrao continuam la, so o que entra e validado
    private static void aplicarEm(MongoTemplate mongoTemplate, String colecao, Document esquema) {
        if (!mongoTemplate.collectionExists(colecao)) {
            mongoTemplate.createCollection(colecao);
        }
        mongoTemplate.getDb().runCommand(new Document("collMod", colecao)
                .append("validator", new Document("$jsonSchema", esquema))
                .append("validationLevel", "moderate")
                .append("validationAction", "error"));
    }

    private static Document objeto(List<String> obrigatorios, Document propriedades) {
        return new Document("bsonType", "object")
                .append("required", obrigatorios)
                .append("properties", propriedades);
    }

    private static Document texto() {
        return new Document("bsonType", "string").append("minLength", 1);
    }

    private static Document enumerado(List<String> valores) {
        return new Document("bsonType", "string").append("enum", valores);
    }

    private static List<String> nomes(Enum<?>[] valores) {
        return Arrays.stream(valores).map(Enum::name).toList();
    }
}

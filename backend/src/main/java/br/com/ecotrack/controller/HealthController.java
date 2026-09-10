package br.com.ecotrack.controller;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final MongoTemplate mongoTemplate;

    public HealthController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/api/health")
    public Map<String, String> verificarSaude() {
        String statusMongo;
        try {
            mongoTemplate.getDb().runCommand(new Document("ping", 1));
            statusMongo = "UP";
        } catch (Exception excecao) {
            statusMongo = "DOWN";
        }
        return Map.of("api", "UP", "mongo", statusMongo);
    }
}

package br.com.ecotrack.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoEsquemaConfig {

    @Bean
    public ApplicationRunner aplicarEsquemas(MongoTemplate mongoTemplate) {
        return args -> EsquemaMongo.aplicar(mongoTemplate);
    }
}

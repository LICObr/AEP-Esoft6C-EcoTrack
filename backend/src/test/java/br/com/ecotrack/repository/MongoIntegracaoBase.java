package br.com.ecotrack.repository;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import br.com.ecotrack.config.MongoConfig;

// um container so pra todas as classes de teste que herdam daqui (precisa do docker rodando)
@DataMongoTest
@Import(MongoConfig.class)
abstract class MongoIntegracaoBase {

    static final MongoDBContainer MONGO = new MongoDBContainer("mongo:7");

    static {
        MONGO.start();
    }

    @DynamicPropertySource
    static void apontarParaOContainer(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO::getReplicaSetUrl);
    }
}

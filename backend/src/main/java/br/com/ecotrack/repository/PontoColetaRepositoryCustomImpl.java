package br.com.ecotrack.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import br.com.ecotrack.domain.model.PontoColeta;

public class PontoColetaRepositoryCustomImpl implements PontoColetaRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public PontoColetaRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<String> listarCidadesAtivas() {
        Query somenteAtivos = Query.query(Criteria.where("ativo").is(true));

        List<String> cidades = mongoTemplate.findDistinct(
                somenteAtivos, "endereco.cidade", PontoColeta.class, String.class);

        return cidades.stream().sorted().toList();
    }
}

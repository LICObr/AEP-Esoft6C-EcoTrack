package br.com.ecotrack.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import br.com.ecotrack.domain.model.Descarte;
import br.com.ecotrack.domain.model.TotalPorMaterial;

public class DescarteRepositoryCustomImpl implements DescarteRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public DescarteRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<TotalPorMaterial> totalPorMaterial() {
        Aggregation pipeline = Aggregation.newAggregation(
                // um documento por item da lista de materiais
                Aggregation.unwind("materiais"),
                Aggregation.group("materiais.tipo", "materiais.unidade")
                        .sum("materiais.quantidade").as("quantidade")
                        .count().as("descartes"),
                Aggregation.project("tipo", "unidade", "quantidade", "descartes").andExclude("_id"),
                Aggregation.sort(Sort.by("tipo", "unidade")));

        return mongoTemplate.aggregate(pipeline, Descarte.class, TotalPorMaterial.class)
                .getMappedResults();
    }
}

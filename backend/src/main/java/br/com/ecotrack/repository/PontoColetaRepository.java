package br.com.ecotrack.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.PontoColeta;

public interface PontoColetaRepository
        extends MongoRepository<PontoColeta, String>, PontoColetaRepositoryCustom {

    List<PontoColeta> findByAtivoTrue();

    List<PontoColeta> findByAtivoTrueAndMateriaisAceitosContaining(Material material);

    List<PontoColeta> findByAtivoTrueAndEnderecoCidade(String cidade);

    List<PontoColeta> findByAtivoTrueAndMateriaisAceitosContainingAndEnderecoCidade(Material material, String cidade);
}

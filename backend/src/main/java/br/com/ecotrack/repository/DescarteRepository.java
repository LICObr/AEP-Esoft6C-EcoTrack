package br.com.ecotrack.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.ecotrack.domain.model.Descarte;

public interface DescarteRepository extends MongoRepository<Descarte, String> {

    List<Descarte> findByUsuarioIdOrderByDataDesc(String usuarioId);
}

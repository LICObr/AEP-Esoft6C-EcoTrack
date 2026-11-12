package br.com.ecotrack.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.ecotrack.domain.exception.RecursoNaoEncontradoException;
import br.com.ecotrack.domain.model.Endereco;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.PontoColeta;
import br.com.ecotrack.repository.PontoColetaRepository;

@Service
public class PontoColetaService {

    private final PontoColetaRepository pontoColetaRepository;

    public PontoColetaService(PontoColetaRepository pontoColetaRepository) {
        this.pontoColetaRepository = pontoColetaRepository;
    }

    public PontoColeta cadastrar(String nome, Endereco endereco, Set<Material> materiaisAceitos) {
        PontoColeta ponto = new PontoColeta(nome, endereco, materiaisAceitos);
        return pontoColetaRepository.save(ponto);
    }

    public PontoColeta buscarPorId(String id) {
        return pontoColetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta nao encontrado."));
    }

    public List<PontoColeta> listarAtivos() {
        return pontoColetaRepository.findByAtivoTrue();
    }

    public List<PontoColeta> filtrarPorMaterial(Material material) {
        return pontoColetaRepository.findByAtivoTrueAndMateriaisAceitosContaining(material);
    }

    // os dois filtros sao opcionais, null significa "sem filtro"
    public List<PontoColeta> filtrar(Material material, String cidade) {
        if (material != null && cidade != null) {
            return pontoColetaRepository
                    .findByAtivoTrueAndMateriaisAceitosContainingAndEnderecoCidade(material, cidade);
        }
        if (material != null) {
            return filtrarPorMaterial(material);
        }
        if (cidade != null) {
            return pontoColetaRepository.findByAtivoTrueAndEnderecoCidade(cidade);
        }
        return listarAtivos();
    }

    public List<String> listarCidades() {
        return pontoColetaRepository.listarCidadesAtivas();
    }

    public PontoColeta atualizar(String id, String nome, Endereco endereco, Set<Material> materiaisAceitos) {
        PontoColeta ponto = buscarPorId(id);
        ponto.atualizar(nome, endereco, materiaisAceitos);
        return pontoColetaRepository.save(ponto);
    }

    public void desativar(String id) {
        PontoColeta ponto = buscarPorId(id);
        ponto.desativar();
        pontoColetaRepository.save(ponto);
    }
}

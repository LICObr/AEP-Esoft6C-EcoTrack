package br.com.ecotrack.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.PontoColeta;
import br.com.ecotrack.dto.CriarPontoColetaRequest;
import br.com.ecotrack.dto.PontoColetaResponse;
import br.com.ecotrack.mapper.EnderecoMapper;
import br.com.ecotrack.mapper.PontoColetaMapper;
import br.com.ecotrack.service.PontoColetaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pontos")
public class PontoColetaController {

    private final PontoColetaService pontoColetaService;

    public PontoColetaController(PontoColetaService pontoColetaService) {
        this.pontoColetaService = pontoColetaService;
    }

    @PostMapping
    public ResponseEntity<PontoColetaResponse> cadastrar(@Valid @RequestBody CriarPontoColetaRequest requisicao) {
        PontoColeta ponto = pontoColetaService.cadastrar(
                requisicao.nome(),
                EnderecoMapper.paraDominio(requisicao.endereco()),
                requisicao.materiaisAceitos());
        return ResponseEntity.status(HttpStatus.CREATED).body(PontoColetaMapper.paraResposta(ponto));
    }

    @GetMapping
    public ResponseEntity<List<PontoColetaResponse>> listarAtivos(
            @RequestParam(required = false) Material material) {
        List<PontoColeta> pontos = material == null
                ? pontoColetaService.listarAtivos()
                : pontoColetaService.filtrarPorMaterial(material);
        List<PontoColetaResponse> resposta = pontos.stream()
                .map(PontoColetaMapper::paraResposta)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColetaResponse> buscarPorId(@PathVariable String id) {
        PontoColeta ponto = pontoColetaService.buscarPorId(id);
        return ResponseEntity.ok(PontoColetaMapper.paraResposta(ponto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoColetaResponse> atualizar(@PathVariable String id,
            @Valid @RequestBody CriarPontoColetaRequest requisicao) {
        PontoColeta ponto = pontoColetaService.atualizar(
                id,
                requisicao.nome(),
                EnderecoMapper.paraDominio(requisicao.endereco()),
                requisicao.materiaisAceitos());
        return ResponseEntity.ok(PontoColetaMapper.paraResposta(ponto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable String id) {
        pontoColetaService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}

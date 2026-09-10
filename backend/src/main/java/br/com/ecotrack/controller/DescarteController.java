package br.com.ecotrack.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ecotrack.domain.model.Descarte;
import br.com.ecotrack.dto.DescarteResponse;
import br.com.ecotrack.dto.RegistrarDescarteRequest;
import br.com.ecotrack.mapper.DescarteMapper;
import br.com.ecotrack.service.DescarteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/descartes")
public class DescarteController {

    private final DescarteService descarteService;

    public DescarteController(DescarteService descarteService) {
        this.descarteService = descarteService;
    }

    @PostMapping
    public ResponseEntity<DescarteResponse> registrar(@Valid @RequestBody RegistrarDescarteRequest requisicao) {
        Descarte descarte = descarteService.registrar(
                requisicao.usuarioId(),
                requisicao.pontoColetaId(),
                DescarteMapper.paraItens(requisicao.materiais()));
        return ResponseEntity.status(HttpStatus.CREATED).body(DescarteMapper.paraResposta(descarte));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DescarteResponse>> listarHistoricoDoUsuario(@PathVariable String usuarioId) {
        List<DescarteResponse> historico = descarteService.listarHistoricoDoUsuario(usuarioId).stream()
                .map(DescarteMapper::paraResposta)
                .toList();
        return ResponseEntity.ok(historico);
    }
}

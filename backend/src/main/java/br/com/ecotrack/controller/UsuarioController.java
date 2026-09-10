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

import br.com.ecotrack.domain.model.Usuario;
import br.com.ecotrack.dto.CriarUsuarioRequest;
import br.com.ecotrack.dto.UsuarioResponse;
import br.com.ecotrack.mapper.UsuarioMapper;
import br.com.ecotrack.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CriarUsuarioRequest requisicao) {
        Usuario usuario = usuarioService.cadastrar(requisicao.nome(), requisicao.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.paraResposta(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable String id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.paraResposta(usuario));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos().stream()
                .map(UsuarioMapper::paraResposta)
                .toList();
        return ResponseEntity.ok(usuarios);
    }
}

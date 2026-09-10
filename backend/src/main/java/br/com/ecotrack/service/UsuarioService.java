package br.com.ecotrack.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ecotrack.domain.exception.ConflitoException;
import br.com.ecotrack.domain.exception.RecursoNaoEncontradoException;
import br.com.ecotrack.domain.model.Usuario;
import br.com.ecotrack.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrar(String nome, String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflitoException("Ja existe um usuario cadastrado com este email.");
        }
        Usuario usuario = new Usuario(nome, email);
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado."));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}

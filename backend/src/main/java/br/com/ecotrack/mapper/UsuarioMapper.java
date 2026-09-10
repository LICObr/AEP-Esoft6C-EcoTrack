package br.com.ecotrack.mapper;

import br.com.ecotrack.domain.model.Usuario;
import br.com.ecotrack.dto.UsuarioResponse;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioResponse paraResposta(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}

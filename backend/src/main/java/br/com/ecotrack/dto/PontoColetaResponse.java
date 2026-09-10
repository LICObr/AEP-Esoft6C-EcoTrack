package br.com.ecotrack.dto;

import java.util.Set;

import br.com.ecotrack.domain.model.Material;

public record PontoColetaResponse(
        String id,
        String nome,
        EnderecoDto endereco,
        Set<Material> materiaisAceitos,
        boolean ativo) {
}

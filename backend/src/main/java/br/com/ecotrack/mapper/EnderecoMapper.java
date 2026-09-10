package br.com.ecotrack.mapper;

import br.com.ecotrack.domain.model.Endereco;
import br.com.ecotrack.dto.EnderecoDto;

public final class EnderecoMapper {

    private EnderecoMapper() {
    }

    public static Endereco paraDominio(EnderecoDto dto) {
        return new Endereco(dto.rua(), dto.numero(), dto.bairro(), dto.cidade(), dto.estado());
    }

    public static EnderecoDto paraDto(Endereco endereco) {
        return new EnderecoDto(
                endereco.rua(),
                endereco.numero(),
                endereco.bairro(),
                endereco.cidade(),
                endereco.estado());
    }
}

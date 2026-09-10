package br.com.ecotrack.mapper;

import br.com.ecotrack.domain.model.PontoColeta;
import br.com.ecotrack.dto.PontoColetaResponse;

public final class PontoColetaMapper {

    private PontoColetaMapper() {
    }

    public static PontoColetaResponse paraResposta(PontoColeta ponto) {
        return new PontoColetaResponse(
                ponto.getId(),
                ponto.getNome(),
                EnderecoMapper.paraDto(ponto.getEndereco()),
                ponto.getMateriaisAceitos(),
                ponto.isAtivo());
    }
}

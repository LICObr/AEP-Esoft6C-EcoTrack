package br.com.ecotrack.mapper;

import java.util.List;

import br.com.ecotrack.domain.model.Descarte;
import br.com.ecotrack.domain.model.ItemDescarte;
import br.com.ecotrack.dto.DescarteResponse;
import br.com.ecotrack.dto.ItemDescarteDto;

public final class DescarteMapper {

    private DescarteMapper() {
    }

    public static List<ItemDescarte> paraItens(List<ItemDescarteDto> requisicoes) {
        return requisicoes.stream()
                .map(item -> new ItemDescarte(item.tipo(), item.quantidade(), item.unidade()))
                .toList();
    }

    public static DescarteResponse paraResposta(Descarte descarte) {
        List<ItemDescarteDto> materiais = descarte.getMateriais().stream()
                .map(item -> new ItemDescarteDto(item.tipo(), item.quantidade(), item.unidade()))
                .toList();

        return new DescarteResponse(
                descarte.getId(),
                descarte.getUsuarioId(),
                descarte.getPontoColetaId(),
                materiais,
                descarte.getData());
    }
}

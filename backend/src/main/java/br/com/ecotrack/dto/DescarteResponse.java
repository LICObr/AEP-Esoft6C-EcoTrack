package br.com.ecotrack.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DescarteResponse(
        String id,
        String usuarioId,
        String pontoColetaId,
        List<ItemDescarteDto> materiais,
        LocalDateTime data) {
}

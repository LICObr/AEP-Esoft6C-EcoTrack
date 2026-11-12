package br.com.ecotrack.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DescarteResponse(
        String id,
        String usuarioId,
        String usuarioNome,
        String pontoColetaId,
        String pontoColetaNome,
        List<ItemDescarteDto> materiais,
        LocalDateTime data) {
}

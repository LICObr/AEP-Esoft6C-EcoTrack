package br.com.ecotrack.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RegistrarDescarteRequest(
        @NotBlank(message = "O usuario e obrigatorio") String usuarioId,
        @NotBlank(message = "O ponto de coleta e obrigatorio") String pontoColetaId,
        @NotEmpty(message = "Informe pelo menos um material")
        @Valid List<ItemDescarteDto> materiais) {
}

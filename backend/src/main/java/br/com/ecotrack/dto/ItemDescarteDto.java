package br.com.ecotrack.dto;

import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.Unidade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemDescarteDto(
        @NotNull(message = "O tipo de material e obrigatorio") Material tipo,
        @Min(value = 1, message = "A quantidade deve ser maior que zero") int quantidade,
        @NotNull(message = "A unidade e obrigatoria") Unidade unidade) {
}

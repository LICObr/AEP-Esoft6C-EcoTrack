package br.com.ecotrack.dto;

import java.util.Set;

import br.com.ecotrack.domain.model.Material;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CriarPontoColetaRequest(
        @NotBlank(message = "O nome e obrigatorio") String nome,
        @NotNull(message = "O endereco e obrigatorio") @Valid EnderecoDto endereco,
        @NotEmpty(message = "Informe pelo menos um material aceito") Set<Material> materiaisAceitos) {
}

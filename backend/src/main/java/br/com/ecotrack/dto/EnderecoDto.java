package br.com.ecotrack.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecoDto(
        @NotBlank(message = "A rua e obrigatoria") String rua,
        String numero,
        String bairro,
        @NotBlank(message = "A cidade e obrigatoria") String cidade,
        @NotBlank(message = "O estado e obrigatorio") String estado) {
}

package br.com.ecotrack.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CriarUsuarioRequest(
        @NotBlank(message = "O nome e obrigatorio") String nome,
        @NotBlank(message = "O email e obrigatorio")
        @Email(message = "O email deve ser valido") String email) {
}

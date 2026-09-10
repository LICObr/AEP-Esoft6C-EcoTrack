package br.com.ecotrack.domain.model;

public record Endereco(String rua, String numero, String bairro, String cidade, String estado) {

    public Endereco {
        if (rua == null || rua.isBlank()) {
            throw new IllegalArgumentException("A rua do endereco e obrigatoria.");
        }
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("A cidade do endereco e obrigatoria.");
        }
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("O estado do endereco e obrigatorio.");
        }
    }
}

package br.com.ecotrack.domain.model;

public record ItemDescarte(Material tipo, int quantidade, Unidade unidade) {

    public ItemDescarte {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de material do item e obrigatorio.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade do item deve ser maior que zero.");
        }
        if (unidade == null) {
            throw new IllegalArgumentException("A unidade do item e obrigatoria.");
        }
    }
}

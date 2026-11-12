package br.com.ecotrack.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pontos_coleta")
@CompoundIndexes({
        // filtro de pontos ativos por material
        @CompoundIndex(name = "ativo_materiais", def = "{'ativo': 1, 'materiaisAceitos': 1}"),
        // filtro de pontos ativos por cidade (a cidade vem de um select, entao busca exata)
        @CompoundIndex(name = "ativo_cidade", def = "{'ativo': 1, 'endereco.cidade': 1}")
})
public class PontoColeta {

    @Id
    private String id;

    private String nome;

    private Endereco endereco;

    private Set<Material> materiaisAceitos;

    private boolean ativo;

    @CreatedDate
    private LocalDateTime criadoEm;

    public PontoColeta(String nome, Endereco endereco, Set<Material> materiaisAceitos) {
        validarDados(nome, endereco, materiaisAceitos);
        this.nome = nome;
        this.endereco = endereco;
        this.materiaisAceitos = new HashSet<>(materiaisAceitos);
        this.ativo = true;
    }

    public boolean aceita(Material material) {
        return materiaisAceitos.contains(material);
    }

    public void desativar() {
        this.ativo = false;
    }

    public void atualizar(String nome, Endereco endereco, Set<Material> materiaisAceitos) {
        validarDados(nome, endereco, materiaisAceitos);
        this.nome = nome;
        this.endereco = endereco;
        this.materiaisAceitos = new HashSet<>(materiaisAceitos);
    }

    private void validarDados(String nome, Endereco endereco, Set<Material> materiaisAceitos) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do ponto de coleta e obrigatorio.");
        }
        if (endereco == null) {
            throw new IllegalArgumentException("O endereco do ponto de coleta e obrigatorio.");
        }
        if (materiaisAceitos == null || materiaisAceitos.isEmpty()) {
            throw new IllegalArgumentException("O ponto de coleta deve aceitar pelo menos um material.");
        }
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Set<Material> getMateriaisAceitos() {
        return materiaisAceitos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}

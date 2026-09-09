package br.com.ecotrack.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pontos_coleta")
public class PontoColeta {

    @Id
    private String id;

    private String nome;

    private Endereco endereco;

    @Indexed
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

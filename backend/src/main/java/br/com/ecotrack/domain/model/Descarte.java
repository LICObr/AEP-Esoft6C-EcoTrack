package br.com.ecotrack.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.ecotrack.domain.exception.RegraDeNegocioException;

@Document(collection = "descartes")
// historico do usuario: filtra por usuario e ja devolve ordenado pela data
@CompoundIndex(name = "usuario_data", def = "{'usuarioId': 1, 'data': -1}")
public class Descarte {

    @Id
    private String id;

    private String usuarioId;

    // nome copiado na hora do descarte, o historico nao muda se o cadastro mudar
    private String usuarioNome;

    private String pontoColetaId;

    private String pontoColetaNome;

    private List<ItemDescarte> materiais;

    @CreatedDate
    private LocalDateTime data;

    public Descarte(String usuarioId, String usuarioNome, String pontoColetaId,
                    String pontoColetaNome, List<ItemDescarte> materiais) {
        exigirTexto(usuarioId, "O usuario do descarte e obrigatorio.");
        exigirTexto(usuarioNome, "O nome do usuario do descarte e obrigatorio.");
        exigirTexto(pontoColetaId, "O ponto de coleta do descarte e obrigatorio.");
        exigirTexto(pontoColetaNome, "O nome do ponto de coleta do descarte e obrigatorio.");
        if (materiais == null || materiais.isEmpty()) {
            throw new IllegalArgumentException("O descarte deve ter pelo menos um material.");
        }
        if (temMaterialRepetido(materiais)) {
            throw new IllegalArgumentException("O descarte nao pode ter o mesmo material repetido.");
        }
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.pontoColetaId = pontoColetaId;
        this.pontoColetaNome = pontoColetaNome;
        this.materiais = materiais;
    }

    public int totalItens() {
        return materiais.stream().mapToInt(ItemDescarte::quantidade).sum();
    }

    public void validarContra(PontoColeta ponto) {
        if (!ponto.isAtivo()) {
            throw new RegraDeNegocioException("O ponto de coleta esta inativo.");
        }
        for (ItemDescarte item : materiais) {
            if (!ponto.aceita(item.tipo())) {
                throw new RegraDeNegocioException(
                        "O ponto de coleta nao aceita o material " + item.tipo() + ".");
            }
        }
    }

    private boolean temMaterialRepetido(List<ItemDescarte> itens) {
        long tiposDiferentes = itens.stream().map(ItemDescarte::tipo).distinct().count();
        return tiposDiferentes != itens.size();
    }

    private void exigirTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    public String getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public String getPontoColetaId() {
        return pontoColetaId;
    }

    public String getPontoColetaNome() {
        return pontoColetaNome;
    }

    public List<ItemDescarte> getMateriais() {
        return materiais;
    }

    public LocalDateTime getData() {
        return data;
    }
}

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

    private String pontoColetaId;

    private List<ItemDescarte> materiais;

    @CreatedDate
    private LocalDateTime data;

    public Descarte(String usuarioId, String pontoColetaId, List<ItemDescarte> materiais) {
        if (usuarioId == null || usuarioId.isBlank()) {
            throw new IllegalArgumentException("O usuario do descarte e obrigatorio.");
        }
        if (pontoColetaId == null || pontoColetaId.isBlank()) {
            throw new IllegalArgumentException("O ponto de coleta do descarte e obrigatorio.");
        }
        if (materiais == null || materiais.isEmpty()) {
            throw new IllegalArgumentException("O descarte deve ter pelo menos um material.");
        }
        this.usuarioId = usuarioId;
        this.pontoColetaId = pontoColetaId;
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

    public String getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public String getPontoColetaId() {
        return pontoColetaId;
    }

    public List<ItemDescarte> getMateriais() {
        return materiais;
    }

    public LocalDateTime getData() {
        return data;
    }
}

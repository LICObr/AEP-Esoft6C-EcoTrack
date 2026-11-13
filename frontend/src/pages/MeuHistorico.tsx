import { useEffect, useState } from "react";
import { listarHistorico } from "../api/descartes";
import { listarUsuarios } from "../api/usuarios";
import { TituloPagina } from "../components/TituloPagina";
import type { Descarte, Usuario } from "../api/tipos";

export function MeuHistorico() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [usuarioId, setUsuarioId] = useState("");
  const [historico, setHistorico] = useState<Descarte[]>([]);

  useEffect(() => {
    listarUsuarios().then(setUsuarios);
  }, []);

  useEffect(() => {
    if (!usuarioId) return;
    listarHistorico(usuarioId).then(setHistorico);
  }, [usuarioId]);

  // sem usuario escolhido nao mostra nada
  const descartes = usuarioId ? historico : [];

  return (
    <section className="pagina">
      <TituloPagina
        etiqueta="Sua jornada"
        titulo="Meu histórico"
        descricao="Tudo o que você já descartou, do mais recente para o mais antigo."
      />

      <div className="filtros cartao">
        <div className="campo">
          <label htmlFor="usuario-historico">Usuário</label>
          <select id="usuario-historico" value={usuarioId} onChange={(e) => setUsuarioId(e.target.value)}>
            <option value="">Selecione</option>
            {usuarios.map((usuario) => (
              <option key={usuario.id} value={usuario.id}>{usuario.nome}</option>
            ))}
          </select>
        </div>
      </div>

      {!usuarioId && <p className="vazio">Escolha um usuário para ver o histórico.</p>}

      <ul className="linha-do-tempo">
        {descartes.map((descarte, posicao) => {
          const data = new Date(descarte.data);
          return (
            <li key={descarte.id} className="descarte cartao" style={{ animationDelay: `${posicao * 45}ms` }}>
              <time className="descarte__data" dateTime={descarte.data}>
                <strong>{data.toLocaleDateString("pt-BR", { day: "2-digit" })}</strong>
                <span>{data.toLocaleDateString("pt-BR", { month: "short", year: "numeric" })}</span>
              </time>
              <div className="descarte__corpo">
                <h3>{descarte.pontoColetaNome ?? "ponto não informado"}</h3>
                <div className="descarte__itens">
                  {descarte.materiais.map((item) => (
                    <span key={`${item.tipo}-${item.unidade}`} className="chip">
                      {item.quantidade} {item.unidade} de {item.tipo}
                    </span>
                  ))}
                </div>
              </div>
            </li>
          );
        })}
      </ul>
    </section>
  );
}

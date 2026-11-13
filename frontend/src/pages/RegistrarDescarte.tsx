import { useEffect, useState } from "react";
import { registrarDescarte } from "../api/descartes";
import { ErroDeRequisicao } from "../api/cliente";
import { listarPontos } from "../api/pontosColeta";
import { listarUsuarios } from "../api/usuarios";
import { TituloPagina } from "../components/TituloPagina";
import { MATERIAIS, UNIDADES } from "../api/constantes";
import type { Material, PontoColeta, Unidade, Usuario } from "../api/tipos";

export function RegistrarDescarte() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [pontos, setPontos] = useState<PontoColeta[]>([]);
  const [usuarioId, setUsuarioId] = useState("");
  const [pontoColetaId, setPontoColetaId] = useState("");
  const [material, setMaterial] = useState<Material>("PILHAS");
  const [quantidade, setQuantidade] = useState(1);
  const [unidade, setUnidade] = useState<Unidade>("UNIDADE");
  const [aviso, setAviso] = useState<{ texto: string; erro: boolean } | null>(null);
  const [enviando, setEnviando] = useState(false);

  useEffect(() => {
    listarUsuarios().then(setUsuarios);
    listarPontos().then(setPontos);
  }, []);

  async function registrar(evento: React.FormEvent) {
    evento.preventDefault();
    setAviso(null);
    setEnviando(true);

    try {
      await registrarDescarte(usuarioId, pontoColetaId, [{ tipo: material, quantidade, unidade }]);
      setAviso({ texto: "Descarte registrado com sucesso!", erro: false });
    } catch (erro) {
      if (erro instanceof ErroDeRequisicao) {
        setAviso({ texto: `Erro (${erro.status}): ${erro.message}`, erro: true });
      } else {
        setAviso({ texto: "Erro inesperado ao registrar o descarte.", erro: true });
      }
    } finally {
      setEnviando(false);
    }
  }

  return (
    <section className="pagina">
      <TituloPagina
        etiqueta="Seu impacto"
        titulo="Registrar descarte"
        descricao="Conte o que você levou até um ponto de coleta. A gente confere se o ponto aceita o material."
      />

      <form className="formulario cartao" onSubmit={registrar}>
        <div className="grade-campos">
          <div className="campo">
            <label htmlFor="usuario">Usuário</label>
            <select id="usuario" value={usuarioId} onChange={(e) => setUsuarioId(e.target.value)} required>
              <option value="">Selecione</option>
              {usuarios.map((usuario) => (
                <option key={usuario.id} value={usuario.id}>{usuario.nome}</option>
              ))}
            </select>
          </div>

          <div className="campo">
            <label htmlFor="ponto">Ponto de coleta</label>
            <select id="ponto" value={pontoColetaId} onChange={(e) => setPontoColetaId(e.target.value)} required>
              <option value="">Selecione</option>
              {pontos.map((ponto) => (
                <option key={ponto.id} value={ponto.id}>{ponto.nome}</option>
              ))}
            </select>
          </div>

          <div className="campo campo--cheio">
            <label htmlFor="material">Material</label>
            <select id="material" value={material} onChange={(e) => setMaterial(e.target.value as Material)}>
              {MATERIAIS.map((m) => (
                <option key={m} value={m}>{m}</option>
              ))}
            </select>
          </div>

          <div className="campo">
            <label htmlFor="quantidade">Quantidade</label>
            <input
              id="quantidade"
              type="number"
              min={1}
              value={quantidade}
              onChange={(e) => setQuantidade(Number(e.target.value))}
            />
          </div>

          <div className="campo">
            <label htmlFor="unidade">Unidade</label>
            <select id="unidade" value={unidade} onChange={(e) => setUnidade(e.target.value as Unidade)}>
              {UNIDADES.map((u) => (
                <option key={u} value={u}>{u}</option>
              ))}
            </select>
          </div>
        </div>

        <div className="formulario__acoes">
          <button type="submit" className="botao" disabled={enviando}>Registrar</button>
        </div>

        {aviso && (
          <p role="status" className={aviso.erro ? "aviso aviso--erro" : "aviso"}>{aviso.texto}</p>
        )}
      </form>
    </section>
  );
}

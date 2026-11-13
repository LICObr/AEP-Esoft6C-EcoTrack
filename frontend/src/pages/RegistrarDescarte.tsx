import { useEffect, useState } from "react";
import { registrarDescarte } from "../api/descartes";
import { ErroDeRequisicao } from "../api/cliente";
import { listarPontos } from "../api/pontosColeta";
import { listarUsuarios } from "../api/usuarios";
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
  const [mensagem, setMensagem] = useState("");

  useEffect(() => {
    listarUsuarios().then(setUsuarios);
    listarPontos().then(setPontos);
  }, []);

  async function registrar(evento: React.FormEvent) {
    evento.preventDefault();
    setMensagem("");

    try {
      await registrarDescarte(usuarioId, pontoColetaId, [{ tipo: material, quantidade, unidade }]);
      setMensagem("Descarte registrado com sucesso!");
    } catch (erro) {
      if (erro instanceof ErroDeRequisicao) {
        setMensagem(`Erro (${erro.status}): ${erro.message}`);
      } else {
        setMensagem("Erro inesperado ao registrar o descarte.");
      }
    }
  }

  return (
    <section>
      <h2>Registrar descarte</h2>

      <form onSubmit={registrar}>
        <label htmlFor="usuario">Usuário</label>
        <select id="usuario" value={usuarioId} onChange={(e) => setUsuarioId(e.target.value)} required>
          <option value="">Selecione</option>
          {usuarios.map((usuario) => (
            <option key={usuario.id} value={usuario.id}>{usuario.nome}</option>
          ))}
        </select>

        <label htmlFor="ponto">Ponto de coleta</label>
        <select id="ponto" value={pontoColetaId} onChange={(e) => setPontoColetaId(e.target.value)} required>
          <option value="">Selecione</option>
          {pontos.map((ponto) => (
            <option key={ponto.id} value={ponto.id}>{ponto.nome}</option>
          ))}
        </select>

        <label htmlFor="material">Material</label>
        <select id="material" value={material} onChange={(e) => setMaterial(e.target.value as Material)}>
          {MATERIAIS.map((m) => (
            <option key={m} value={m}>{m}</option>
          ))}
        </select>

        <label htmlFor="quantidade">Quantidade</label>
        <input
          id="quantidade"
          type="number"
          min={1}
          value={quantidade}
          onChange={(e) => setQuantidade(Number(e.target.value))}
        />

        <label htmlFor="unidade">Unidade</label>
        <select id="unidade" value={unidade} onChange={(e) => setUnidade(e.target.value as Unidade)}>
          {UNIDADES.map((u) => (
            <option key={u} value={u}>{u}</option>
          ))}
        </select>

        <button type="submit">Registrar</button>
      </form>

      {mensagem && <p role="status">{mensagem}</p>}
    </section>
  );
}

import { useEffect, useState } from "react";
import { listarHistorico } from "../api/descartes";
import { listarUsuarios } from "../api/usuarios";
import type { Descarte, Usuario } from "../api/tipos";

export function MeuHistorico() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [usuarioId, setUsuarioId] = useState("");
  const [historico, setHistorico] = useState<Descarte[]>([]);

  useEffect(() => {
    listarUsuarios().then(setUsuarios);
  }, []);

  useEffect(() => {
    if (usuarioId) {
      listarHistorico(usuarioId).then(setHistorico);
    } else {
      setHistorico([]);
    }
  }, [usuarioId]);

  return (
    <section>
      <h2>Meu histórico</h2>

      <label htmlFor="usuario-historico">Usuário</label>
      <select id="usuario-historico" value={usuarioId} onChange={(e) => setUsuarioId(e.target.value)}>
        <option value="">Selecione</option>
        {usuarios.map((usuario) => (
          <option key={usuario.id} value={usuario.id}>{usuario.nome}</option>
        ))}
      </select>

      <ul>
        {historico.map((descarte) => (
          <li key={descarte.id}>
            {new Date(descarte.data).toLocaleDateString("pt-BR")} —{" "}
            {descarte.materiais.map((item) => `${item.quantidade} ${item.unidade} de ${item.tipo}`).join(", ")}
          </li>
        ))}
      </ul>
    </section>
  );
}

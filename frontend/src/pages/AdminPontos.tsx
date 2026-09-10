import { useEffect, useState } from "react";
import { cadastrarPonto, desativarPonto, listarPontos } from "../api/pontosColeta";
import type { Material, PontoColeta } from "../api/tipos";

const MATERIAIS: Material[] = [
  "PILHAS", "ELETRONICOS", "OLEO", "PLASTICO", "PAPEL", "VIDRO", "METAL",
];

export function AdminPontos() {
  const [pontos, setPontos] = useState<PontoColeta[]>([]);
  const [nome, setNome] = useState("");
  const [rua, setRua] = useState("");
  const [cidade, setCidade] = useState("Maringá");
  const [estado, setEstado] = useState("PR");
  const [materiais, setMateriais] = useState<Material[]>([]);

  function carregar() {
    listarPontos().then(setPontos);
  }

  useEffect(() => {
    carregar();
  }, []);

  function alternarMaterial(material: Material) {
    setMateriais((atual) =>
      atual.includes(material) ? atual.filter((m) => m !== material) : [...atual, material]
    );
  }

  async function cadastrar(evento: React.FormEvent) {
    evento.preventDefault();
    await cadastrarPonto(nome, { rua, numero: "", bairro: "", cidade, estado }, materiais);
    setNome("");
    setRua("");
    setMateriais([]);
    carregar();
  }

  async function desativar(id: string) {
    await desativarPonto(id);
    carregar();
  }

  return (
    <section>
      <h2>Admin de pontos</h2>

      <form onSubmit={cadastrar}>
        <label htmlFor="nome-ponto">Nome</label>
        <input id="nome-ponto" value={nome} onChange={(e) => setNome(e.target.value)} required />

        <label htmlFor="rua-ponto">Rua</label>
        <input id="rua-ponto" value={rua} onChange={(e) => setRua(e.target.value)} required />

        <label htmlFor="cidade-ponto">Cidade</label>
        <input id="cidade-ponto" value={cidade} onChange={(e) => setCidade(e.target.value)} required />

        <label htmlFor="estado-ponto">Estado</label>
        <input id="estado-ponto" value={estado} onChange={(e) => setEstado(e.target.value)} required />

        <fieldset>
          <legend>Materiais aceitos</legend>
          {MATERIAIS.map((material) => (
            <label key={material}>
              <input
                type="checkbox"
                checked={materiais.includes(material)}
                onChange={() => alternarMaterial(material)}
              />
              {material}
            </label>
          ))}
        </fieldset>

        <button type="submit">Cadastrar ponto</button>
      </form>

      <ul>
        {pontos.map((ponto) => (
          <li key={ponto.id}>
            {ponto.nome} — {ponto.ativo ? "ativo" : "inativo"}{" "}
            {ponto.ativo && <button onClick={() => desativar(ponto.id)}>Desativar</button>}
          </li>
        ))}
      </ul>
    </section>
  );
}

import { useEffect, useState } from "react";
import { listarCidades, listarPontos } from "../api/pontosColeta";
import { MATERIAIS } from "../api/constantes";
import type { Material, PontoColeta } from "../api/tipos";

export function PontosColeta() {
  const [pontos, setPontos] = useState<PontoColeta[]>([]);
  const [cidades, setCidades] = useState<string[]>([]);
  const [materialSelecionado, setMaterialSelecionado] = useState<Material | "">("");
  const [cidadeSelecionada, setCidadeSelecionada] = useState("");
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    listarCidades().then(setCidades);
  }, []);

  useEffect(() => {
    setCarregando(true);
    listarPontos(materialSelecionado || undefined, cidadeSelecionada || undefined)
      .then(setPontos)
      .finally(() => setCarregando(false));
  }, [materialSelecionado, cidadeSelecionada]);

  return (
    <section>
      <h2>Pontos de coleta</h2>

      <label htmlFor="filtro-material">Filtrar por material:</label>
      <select
        id="filtro-material"
        value={materialSelecionado}
        onChange={(evento) => setMaterialSelecionado(evento.target.value as Material | "")}
      >
        <option value="">Todos</option>
        {MATERIAIS.map((material) => (
          <option key={material} value={material}>{material}</option>
        ))}
      </select>

      <label htmlFor="filtro-cidade">Cidade:</label>
      <select
        id="filtro-cidade"
        value={cidadeSelecionada}
        onChange={(evento) => setCidadeSelecionada(evento.target.value)}
      >
        <option value="">Todas</option>
        {cidades.map((cidade) => (
          <option key={cidade} value={cidade}>{cidade}</option>
        ))}
      </select>

      {carregando && <p>Carregando...</p>}

      <ul>
        {pontos.map((ponto) => (
          <li key={ponto.id}>
            <strong>{ponto.nome}</strong> — {ponto.endereco.rua}, {ponto.endereco.bairro},{" "}
            {ponto.endereco.cidade}
            <br />
            Materiais: {ponto.materiaisAceitos.join(", ")}
          </li>
        ))}
      </ul>
    </section>
  );
}

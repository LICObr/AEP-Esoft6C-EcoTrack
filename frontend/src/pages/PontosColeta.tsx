import { useEffect, useState } from "react";
import { listarPontos } from "../api/pontosColeta";
import type { Material, PontoColeta } from "../api/tipos";

const MATERIAIS: Material[] = [
  "PILHAS", "ELETRONICOS", "OLEO", "PLASTICO", "PAPEL", "VIDRO", "METAL",
];

export function PontosColeta() {
  const [pontos, setPontos] = useState<PontoColeta[]>([]);
  const [materialSelecionado, setMaterialSelecionado] = useState<Material | "">("");
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    setCarregando(true);
    listarPontos(materialSelecionado || undefined)
      .then(setPontos)
      .finally(() => setCarregando(false));
  }, [materialSelecionado]);

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

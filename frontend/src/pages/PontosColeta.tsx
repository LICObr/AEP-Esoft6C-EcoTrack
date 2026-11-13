import { useEffect, useState } from "react";
import { listarCidades, listarPontos } from "../api/pontosColeta";
import { MATERIAIS } from "../api/constantes";
import { TituloPagina } from "../components/TituloPagina";
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
    listarPontos(materialSelecionado || undefined, cidadeSelecionada || undefined)
      .then(setPontos)
      .finally(() => setCarregando(false));
  }, [materialSelecionado, cidadeSelecionada]);

  function escolherMaterial(material: Material | "") {
    setCarregando(true);
    setMaterialSelecionado(material);
  }

  function escolherCidade(cidade: string) {
    setCarregando(true);
    setCidadeSelecionada(cidade);
  }

  return (
    <section className="pagina">
      <TituloPagina
        etiqueta="Onde descartar"
        titulo="Pontos de coleta"
        descricao="Ache um ponto ativo que aceite o seu material e filtre pela sua cidade."
      />

      <div className="filtros cartao">
        <div className="campo">
          <label htmlFor="filtro-material">Filtrar por material:</label>
          <select
            id="filtro-material"
            value={materialSelecionado}
            onChange={(evento) => escolherMaterial(evento.target.value as Material | "")}
          >
            <option value="">Todos</option>
            {MATERIAIS.map((material) => (
              <option key={material} value={material}>{material}</option>
            ))}
          </select>
        </div>

        <div className="campo">
          <label htmlFor="filtro-cidade">Cidade:</label>
          <select
            id="filtro-cidade"
            value={cidadeSelecionada}
            onChange={(evento) => escolherCidade(evento.target.value)}
          >
            <option value="">Todas</option>
            {cidades.map((cidade) => (
              <option key={cidade} value={cidade}>{cidade}</option>
            ))}
          </select>
        </div>
      </div>

      {carregando && <p className="vazio">Carregando...</p>}
      {!carregando && pontos.length === 0 && (
        <p className="vazio">Nenhum ponto encontrado com esses filtros.</p>
      )}

      <ul className="grade-pontos">
        {pontos.map((ponto, posicao) => (
          <li key={ponto.id} className="ponto cartao" style={{ animationDelay: `${posicao * 45}ms` }}>
            <h3>{ponto.nome}</h3>
            <p className="ponto__endereco">
              {ponto.endereco.rua}
              <br />
              {ponto.endereco.bairro} · {ponto.endereco.cidade}
            </p>
            <div className="ponto__materiais">
              {ponto.materiaisAceitos.map((material) => (
                <span key={material} className="chip">{material}</span>
              ))}
            </div>
          </li>
        ))}
      </ul>
    </section>
  );
}

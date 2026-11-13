import { useEffect, useState } from "react";
import { buscarDashboard } from "../api/dashboard";
import type { DashboardResumo } from "../api/tipos";
import { TituloPagina } from "../components/TituloPagina";

export function Dashboard() {
  const [resumo, setResumo] = useState<DashboardResumo | null>(null);

  useEffect(() => {
    buscarDashboard().then(setResumo);
  }, []);

  if (!resumo) {
    return <p className="vazio">Carregando...</p>;
  }

  return (
    <section className="pagina">
      <TituloPagina
        etiqueta="Resultados"
        titulo="Dashboard"
        descricao="O que já foi descartado, separado por material e por unidade."
      />

      <p className="total cartao">
        Total de descartes: <strong>{resumo.totalDescartes}</strong>
      </p>

      {resumo.porMaterial.length === 0 ? (
        <p className="vazio">Nenhum descarte registrado ainda.</p>
      ) : (
        <div className="cartao tabela-cartao">
          <table className="tabela">
            <thead>
              <tr>
                <th>Material</th>
                <th>Unidade</th>
                <th>Quantidade</th>
                <th>Descartes</th>
              </tr>
            </thead>
            <tbody>
              {resumo.porMaterial.map((total) => (
                <tr key={`${total.tipo}-${total.unidade}`}>
                  <td>{total.tipo}</td>
                  <td>{total.unidade}</td>
                  <td>{total.quantidade}</td>
                  <td>{total.descartes}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}

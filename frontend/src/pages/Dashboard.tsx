import { useEffect, useState } from "react";
import { buscarDashboard } from "../api/dashboard";
import type { DashboardResumo } from "../api/tipos";

export function Dashboard() {
  const [resumo, setResumo] = useState<DashboardResumo | null>(null);

  useEffect(() => {
    buscarDashboard().then(setResumo);
  }, []);

  if (!resumo) {
    return <p>Carregando...</p>;
  }

  return (
    <section>
      <h2>Dashboard</h2>
      <p>Total de descartes: <strong>{resumo.totalDescartes}</strong></p>

      {resumo.porMaterial.length === 0 ? (
        <p>Nenhum descarte registrado ainda.</p>
      ) : (
        <table>
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
      )}
    </section>
  );
}

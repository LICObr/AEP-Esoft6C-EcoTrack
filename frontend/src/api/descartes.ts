import { requisitar } from "./cliente";
import type { Descarte, ItemDescarte } from "./tipos";

export function registrarDescarte(
  usuarioId: string,
  pontoColetaId: string,
  materiais: ItemDescarte[],
): Promise<Descarte> {
  return requisitar<Descarte>("/api/descartes", {
    method: "POST",
    body: JSON.stringify({ usuarioId, pontoColetaId, materiais }),
  });
}

export function listarHistorico(usuarioId: string): Promise<Descarte[]> {
  return requisitar<Descarte[]>(`/api/descartes/usuario/${usuarioId}`);
}

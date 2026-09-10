import { requisitar } from "./cliente";
import type { Endereco, Material, PontoColeta } from "./tipos";

export function listarPontos(material?: Material): Promise<PontoColeta[]> {
  const query = material ? `?material=${material}` : "";
  return requisitar<PontoColeta[]>(`/api/pontos${query}`);
}

export function cadastrarPonto(
  nome: string,
  endereco: Endereco,
  materiaisAceitos: Material[],
): Promise<PontoColeta> {
  return requisitar<PontoColeta>("/api/pontos", {
    method: "POST",
    body: JSON.stringify({ nome, endereco, materiaisAceitos }),
  });
}

export function atualizarPonto(
  id: string,
  nome: string,
  endereco: Endereco,
  materiaisAceitos: Material[],
): Promise<PontoColeta> {
  return requisitar<PontoColeta>(`/api/pontos/${id}`, {
    method: "PUT",
    body: JSON.stringify({ nome, endereco, materiaisAceitos }),
  });
}

export function desativarPonto(id: string): Promise<void> {
  return requisitar<void>(`/api/pontos/${id}`, { method: "DELETE" });
}

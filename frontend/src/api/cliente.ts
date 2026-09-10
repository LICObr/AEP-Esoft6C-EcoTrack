import type { ErroApi } from "./tipos";

const URL_BASE = import.meta.env.VITE_API_URL;

export class ErroDeRequisicao extends Error {
  public status: number;

  constructor(status: number, mensagem: string) {
    super(mensagem);
    this.name = "ErroDeRequisicao";
    this.status = status;
  }
}

export async function requisitar<T>(
  caminho: string,
  opcoes?: RequestInit,
): Promise<T> {
  const resposta = await fetch(`${URL_BASE}${caminho}`, {
    headers: { "Content-Type": "application/json" },
    ...opcoes,
  });

  if (!resposta.ok) {
    const erro: ErroApi = await resposta.json();
    throw new ErroDeRequisicao(resposta.status, erro.mensagem);
  }

  if (resposta.status === 204) {
    return undefined as T;
  }

  return resposta.json() as Promise<T>;
}

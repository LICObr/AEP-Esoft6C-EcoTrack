import { requisitar } from "./cliente";
import type { Usuario } from "./tipos";

export function listarUsuarios(): Promise<Usuario[]> {
  return requisitar<Usuario[]>("/api/usuarios");
}

export function cadastrarUsuario(nome: string, email: string): Promise<Usuario> {
  return requisitar<Usuario>("/api/usuarios", {
    method: "POST",
    body: JSON.stringify({ nome, email }),
  });
}

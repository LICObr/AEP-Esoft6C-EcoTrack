import type { MATERIAIS, UNIDADES } from "./constantes";

export type Material = (typeof MATERIAIS)[number];

export type Unidade = (typeof UNIDADES)[number];

export interface Endereco {
  rua: string;
  numero: string;
  bairro: string;
  cidade: string;
  estado: string;
}

export interface Usuario {
  id: string;
  nome: string;
  email: string;
}

export interface PontoColeta {
  id: string;
  nome: string;
  endereco: Endereco;
  materiaisAceitos: Material[];
  ativo: boolean;
}

export interface ItemDescarte {
  tipo: Material;
  quantidade: number;
  unidade: Unidade;
}

export interface Descarte {
  id: string;
  usuarioId: string;
  usuarioNome: string | null;
  pontoColetaId: string;
  pontoColetaNome: string | null;
  materiais: ItemDescarte[];
  data: string;
}

export interface TotalPorMaterial {
  tipo: Material;
  unidade: Unidade;
  quantidade: number;
  descartes: number;
}

export interface DashboardResumo {
  totalDescartes: number;
  porMaterial: TotalPorMaterial[];
}

export interface ErroApi {
  timestamp: string;
  status: number;
  erro: string;
  mensagem: string;
  caminho: string;
}

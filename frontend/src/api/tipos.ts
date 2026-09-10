export type Material =
  | "PILHAS"
  | "ELETRONICOS"
  | "OLEO"
  | "PLASTICO"
  | "PAPEL"
  | "VIDRO"
  | "METAL";

export type Unidade = "UNIDADE" | "KG" | "LITRO";

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
  pontoColetaId: string;
  materiais: ItemDescarte[];
  data: string;
}

export interface ErroApi {
  timestamp: string;
  status: number;
  erro: string;
  mensagem: string;
  caminho: string;
}

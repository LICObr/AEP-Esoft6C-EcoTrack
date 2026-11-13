interface TituloPaginaProps {
  etiqueta: string;
  titulo: string;
  descricao: string;
}

export function TituloPagina({ etiqueta, titulo, descricao }: TituloPaginaProps) {
  return (
    <header className="titulo-pagina">
      <span className="etiqueta">{etiqueta}</span>
      <h2>{titulo}</h2>
      <p>{descricao}</p>
    </header>
  );
}

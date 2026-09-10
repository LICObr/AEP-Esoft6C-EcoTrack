interface PlaceholderProps {
  etiqueta: string
  titulo: string
  descricao: string
  destaque: string
}

export function Placeholder({ etiqueta, titulo, descricao, destaque }: PlaceholderProps) {
  return (
    <section className="pagina">
      <div className="pagina__cabecalho">
        <span className="etiqueta">{etiqueta}</span>
        <h1>{titulo}</h1>
        <p>{descricao}</p>
      </div>

      <div className="estado-vazio">
        <span className="estado-vazio__icone" aria-hidden="true">
          ✓
        </span>
        <div>
          <h2>Estrutura preparada</h2>
          <p>{destaque}</p>
        </div>
      </div>
    </section>
  )
}

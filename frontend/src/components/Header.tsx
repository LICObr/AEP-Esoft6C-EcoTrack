import { NavLink } from 'react-router-dom'

const links = [
  { to: '/', label: 'Pontos de coleta', end: true },
  { to: '/registrar', label: 'Registrar descarte' },
  { to: '/historico', label: 'Meu histórico' },
  { to: '/admin', label: 'Admin de pontos' },
]

export function Header() {
  return (
    <header className="cabecalho">
      <div className="cabecalho__conteudo">
        <NavLink className="marca" to="/" aria-label="EcoTrack — início">
          <span className="marca__icone" aria-hidden="true">
            ♻
          </span>
          <span>
            <strong>EcoTrack</strong>
            <small>Descarte responsável</small>
          </span>
        </NavLink>

        <nav className="navegacao" aria-label="Navegação principal">
          {links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              end={link.end}
              className={({ isActive }) =>
                isActive ? 'navegacao__link navegacao__link--ativo' : 'navegacao__link'
              }
            >
              {link.label}
            </NavLink>
          ))}
        </nav>
      </div>
    </header>
  )
}

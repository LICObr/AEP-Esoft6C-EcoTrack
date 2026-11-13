import { render, screen } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import { describe, expect, it } from 'vitest'
import { Header } from './Header'

describe('Header', () => {
  it('mostra o link de cada tela', () => {
    render(
      <MemoryRouter>
        <Header />
      </MemoryRouter>,
    )

    for (const nome of ['Pontos de coleta', 'Registrar descarte', 'Meu histórico', 'Dashboard', 'Admin de pontos']) {
      expect(screen.getByRole('link', { name: nome })).toBeInTheDocument()
    }
  })

  it('marca como ativo so o link da rota atual', () => {
    render(
      <MemoryRouter initialEntries={['/dashboard']}>
        <Header />
      </MemoryRouter>,
    )

    expect(screen.getByRole('link', { name: 'Dashboard' })).toHaveClass('navegacao__link--ativo')
    expect(screen.getByRole('link', { name: 'Pontos de coleta' })).not.toHaveClass('navegacao__link--ativo')
  })
})

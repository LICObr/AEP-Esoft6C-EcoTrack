import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { App } from './App'
import { buscarDashboard } from './api/dashboard'
import { listarHistorico } from './api/descartes'
import { listarCidades, listarPontos } from './api/pontosColeta'
import { listarUsuarios } from './api/usuarios'

vi.mock('./api/dashboard')
vi.mock('./api/descartes')
vi.mock('./api/pontosColeta')
vi.mock('./api/usuarios')

beforeEach(() => {
  window.history.pushState({}, '', '/')
  vi.mocked(listarPontos).mockResolvedValue([])
  vi.mocked(listarCidades).mockResolvedValue([])
  vi.mocked(listarUsuarios).mockResolvedValue([])
  vi.mocked(listarHistorico).mockResolvedValue([])
  vi.mocked(buscarDashboard).mockResolvedValue({ totalDescartes: 0, porMaterial: [] })
})

describe('App', () => {
  it('abre na tela de pontos de coleta', async () => {
    render(<App />)

    expect(await screen.findByRole('heading', { name: 'Pontos de coleta' })).toBeInTheDocument()
  })

  it('abre a tela da rota que veio na url', async () => {
    window.history.pushState({}, '', '/dashboard')

    render(<App />)

    expect(await screen.findByRole('heading', { name: 'Dashboard' })).toBeInTheDocument()
  })

  it('troca de tela quando clica no menu', async () => {
    render(<App />)
    await screen.findByRole('heading', { name: 'Pontos de coleta' })

    await userEvent.click(screen.getByRole('link', { name: 'Admin de pontos' }))

    expect(await screen.findByRole('heading', { name: 'Admin de pontos' })).toBeInTheDocument()
  })
})

import { render, screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import * as api from '../api/dashboard'
import { Dashboard } from './Dashboard'

vi.mock('../api/dashboard')

describe('Dashboard', () => {
  it('mostra o total e uma linha por material e unidade', async () => {
    vi.mocked(api.buscarDashboard).mockResolvedValue({
      totalDescartes: 2,
      porMaterial: [{ tipo: 'PILHAS', unidade: 'UNIDADE', quantidade: 15, descartes: 2 }],
    })

    render(<Dashboard />)

    expect(await screen.findByText('PILHAS')).toBeInTheDocument()
    expect(screen.getByText('15')).toBeInTheDocument()
    expect(screen.getByText(/Total de descartes/)).toHaveTextContent('2')
  })

  it('avisa quando ainda nao tem descarte', async () => {
    vi.mocked(api.buscarDashboard).mockResolvedValue({ totalDescartes: 0, porMaterial: [] })

    render(<Dashboard />)

    expect(await screen.findByText('Nenhum descarte registrado ainda.')).toBeInTheDocument()
  })
})

import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import * as api from '../api/pontosColeta'
import type { PontoColeta } from '../api/tipos'
import { PontosColeta } from './PontosColeta'

vi.mock('../api/pontosColeta')

const ponto: PontoColeta = {
  id: '1',
  nome: 'Eco Ponto Centro',
  endereco: { rua: 'Av. Brasil', numero: '1200', bairro: 'Zona 01', cidade: 'Maringa', estado: 'PR' },
  materiaisAceitos: ['PILHAS'],
  ativo: true,
}

describe('PontosColeta', () => {
  beforeEach(() => {
    vi.mocked(api.listarCidades).mockResolvedValue(['Maringa', 'Sarandi'])
    vi.mocked(api.listarPontos).mockResolvedValue([ponto])
  })

  it('lista os pontos que vieram da api', async () => {
    render(<PontosColeta />)

    expect(await screen.findByText('Eco Ponto Centro')).toBeInTheDocument()
  })

  it('busca de novo com a cidade escolhida no filtro', async () => {
    render(<PontosColeta />)
    await screen.findByText('Eco Ponto Centro')

    await userEvent.selectOptions(screen.getByLabelText('Cidade:'), 'Sarandi')

    await waitFor(() => expect(api.listarPontos).toHaveBeenLastCalledWith(undefined, 'Sarandi'))
  })
})

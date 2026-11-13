import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { listarHistorico } from '../api/descartes'
import type { Descarte } from '../api/tipos'
import { listarUsuarios } from '../api/usuarios'
import { MeuHistorico } from './MeuHistorico'

vi.mock('../api/descartes')
vi.mock('../api/usuarios')

const descarte: Descarte = {
  id: 'd1',
  usuarioId: 'u1',
  usuarioNome: 'Joao Silva',
  pontoColetaId: 'p1',
  pontoColetaNome: 'Eco Ponto Centro',
  materiais: [{ tipo: 'PILHAS', quantidade: 5, unidade: 'UNIDADE' }],
  data: '2026-11-12T10:00:00',
}

async function escolherJoao() {
  render(<MeuHistorico />)
  await screen.findByRole('option', { name: 'Joao Silva' })
  await userEvent.selectOptions(screen.getByLabelText('Usuário'), 'u1')
}

beforeEach(() => {
  vi.mocked(listarUsuarios).mockResolvedValue([{ id: 'u1', nome: 'Joao Silva', email: 'joao@email.com' }])
  vi.mocked(listarHistorico).mockResolvedValue([descarte])
})

describe('MeuHistorico', () => {
  it('pede pra escolher um usuario antes de mostrar o historico', async () => {
    render(<MeuHistorico />)
    await screen.findByRole('option', { name: 'Joao Silva' })

    expect(screen.getByText('Escolha um usuário para ver o histórico.')).toBeInTheDocument()
    expect(listarHistorico).not.toHaveBeenCalled()
  })

  it('mostra o ponto e os materiais dos descartes do usuario escolhido', async () => {
    await escolherJoao()

    expect(await screen.findByText('Eco Ponto Centro')).toBeInTheDocument()
    expect(screen.getByText('5 UNIDADE de PILHAS')).toBeInTheDocument()
    expect(listarHistorico).toHaveBeenCalledWith('u1')
  })

  it('cai em "ponto não informado" nos descartes antigos, que nao tem o nome do ponto', async () => {
    vi.mocked(listarHistorico).mockResolvedValue([{ ...descarte, pontoColetaNome: null }])

    await escolherJoao()

    expect(await screen.findByText('ponto não informado')).toBeInTheDocument()
  })
})

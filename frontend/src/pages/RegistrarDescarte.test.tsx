import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { ErroDeRequisicao } from '../api/cliente'
import * as descartes from '../api/descartes'
import * as pontos from '../api/pontosColeta'
import * as usuarios from '../api/usuarios'
import { RegistrarDescarte } from './RegistrarDescarte'

vi.mock('../api/descartes')
vi.mock('../api/pontosColeta')
vi.mock('../api/usuarios')

describe('RegistrarDescarte', () => {
  beforeEach(() => {
    vi.mocked(usuarios.listarUsuarios).mockResolvedValue([
      { id: 'u1', nome: 'Joao Silva', email: 'joao@email.com' },
    ])
    vi.mocked(pontos.listarPontos).mockResolvedValue([
      {
        id: 'p1',
        nome: 'Eco Ponto Centro',
        endereco: { rua: 'Av. Brasil', numero: '1200', bairro: 'Zona 01', cidade: 'Maringa', estado: 'PR' },
        materiaisAceitos: ['PILHAS'],
        ativo: true,
      },
    ])
  })

  async function preencherEEnviar() {
    render(<RegistrarDescarte />)
    await screen.findByRole('option', { name: 'Joao Silva' })
    await userEvent.selectOptions(screen.getByLabelText('Usuário'), 'u1')
    await userEvent.selectOptions(screen.getByLabelText('Ponto de coleta'), 'p1')
    await userEvent.click(screen.getByRole('button', { name: 'Registrar' }))
  }

  it('mostra a mensagem de sucesso quando a api aceita o descarte', async () => {
    vi.mocked(descartes.registrarDescarte).mockResolvedValue({
      id: 'd1', usuarioId: 'u1', usuarioNome: 'Joao Silva', pontoColetaId: 'p1',
      pontoColetaNome: 'Eco Ponto Centro', materiais: [], data: '2026-11-12T10:00:00',
    })

    await preencherEEnviar()

    expect(await screen.findByText('Descarte registrado com sucesso!')).toBeInTheDocument()
  })

  it('mostra o erro 422 quando o ponto nao aceita o material', async () => {
    vi.mocked(descartes.registrarDescarte).mockRejectedValue(
      new ErroDeRequisicao(422, 'O ponto de coleta nao aceita o material OLEO.'),
    )

    await preencherEEnviar()

    expect(
      await screen.findByText('Erro (422): O ponto de coleta nao aceita o material OLEO.'),
    ).toBeInTheDocument()
  })

  it('mostra uma mensagem generica quando o erro nao vem da api', async () => {
    vi.mocked(descartes.registrarDescarte).mockRejectedValue(new Error('rede caiu'))

    await preencherEEnviar()

    expect(await screen.findByText('Erro inesperado ao registrar o descarte.')).toBeInTheDocument()
  })

  it('envia o material, a quantidade e a unidade que foram escolhidos', async () => {
    vi.mocked(descartes.registrarDescarte).mockResolvedValue({
      id: 'd1', usuarioId: 'u1', usuarioNome: 'Joao Silva', pontoColetaId: 'p1',
      pontoColetaNome: 'Eco Ponto Centro', materiais: [], data: '2026-11-12T10:00:00',
    })
    render(<RegistrarDescarte />)
    await screen.findByRole('option', { name: 'Joao Silva' })

    await userEvent.selectOptions(screen.getByLabelText('Usuário'), 'u1')
    await userEvent.selectOptions(screen.getByLabelText('Ponto de coleta'), 'p1')
    await userEvent.selectOptions(screen.getByLabelText('Material'), 'PAPEL')
    await userEvent.clear(screen.getByLabelText('Quantidade'))
    await userEvent.type(screen.getByLabelText('Quantidade'), '3')
    await userEvent.selectOptions(screen.getByLabelText('Unidade'), 'KG')
    await userEvent.click(screen.getByRole('button', { name: 'Registrar' }))

    await screen.findByText('Descarte registrado com sucesso!')
    expect(descartes.registrarDescarte).toHaveBeenCalledWith('u1', 'p1', [
      { tipo: 'PAPEL', quantidade: 3, unidade: 'KG' },
    ])
  })
})

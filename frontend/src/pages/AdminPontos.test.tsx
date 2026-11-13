import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { atualizarPonto, cadastrarPonto, desativarPonto, listarPontos } from '../api/pontosColeta'
import type { PontoColeta } from '../api/tipos'
import { AdminPontos } from './AdminPontos'

vi.mock('../api/pontosColeta')

const ponto: PontoColeta = {
  id: 'p1',
  nome: 'Eco Ponto Centro',
  endereco: { rua: 'Av. Brasil', numero: '1200', bairro: 'Zona 01', cidade: 'Maringa', estado: 'PR' },
  materiaisAceitos: ['PILHAS'],
  ativo: true,
}

async function abrir() {
  render(<AdminPontos />)
  await screen.findByText('Eco Ponto Centro')
}

beforeEach(() => {
  vi.mocked(listarPontos).mockReset()
  vi.mocked(listarPontos).mockResolvedValue([ponto])
  vi.mocked(cadastrarPonto).mockResolvedValue(ponto)
  vi.mocked(atualizarPonto).mockResolvedValue(ponto)
  vi.mocked(desativarPonto).mockResolvedValue(undefined)
})

describe('AdminPontos', () => {
  it('lista os pontos cadastrados', async () => {
    await abrir()

    expect(screen.getByRole('button', { name: 'Editar' })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: 'Desativar' })).toBeInTheDocument()
  })

  it('cadastra um ponto novo com os materiais marcados', async () => {
    await abrir()

    await userEvent.type(screen.getByLabelText('Nome'), 'Eco Ponto Novo')
    await userEvent.type(screen.getByLabelText('Rua'), 'Rua A')
    await userEvent.click(screen.getByLabelText('VIDRO'))
    await userEvent.click(screen.getByRole('button', { name: 'Cadastrar ponto' }))

    await waitFor(() =>
      expect(cadastrarPonto).toHaveBeenCalledWith(
        'Eco Ponto Novo',
        { rua: 'Rua A', numero: '', bairro: '', cidade: 'Maringá', estado: 'PR' },
        ['VIDRO'],
      ),
    )
    expect(listarPontos).toHaveBeenCalledTimes(2)
  })

  it('desmarca um material quando clica de novo', async () => {
    await abrir()
    const vidro = screen.getByLabelText('VIDRO')

    await userEvent.click(vidro)
    await userEvent.click(vidro)

    expect(vidro).not.toBeChecked()
  })

  it('carrega o ponto no formulario ao editar e salva com a atualizacao', async () => {
    await abrir()

    await userEvent.click(screen.getByRole('button', { name: 'Editar' }))
    expect(screen.getByLabelText('Nome')).toHaveValue('Eco Ponto Centro')
    expect(screen.getByLabelText('PILHAS')).toBeChecked()

    await userEvent.clear(screen.getByLabelText('Nome'))
    await userEvent.type(screen.getByLabelText('Nome'), 'Eco Ponto Renovado')
    await userEvent.click(screen.getByRole('button', { name: 'Salvar alterações' }))

    await waitFor(() =>
      expect(atualizarPonto).toHaveBeenCalledWith('p1', 'Eco Ponto Renovado', ponto.endereco, ['PILHAS']),
    )
    expect(cadastrarPonto).not.toHaveBeenCalled()
  })

  it('cancelar a edicao volta pro formulario vazio', async () => {
    await abrir()
    await userEvent.click(screen.getByRole('button', { name: 'Editar' }))

    await userEvent.click(screen.getByRole('button', { name: 'Cancelar edição' }))

    expect(screen.getByLabelText('Nome')).toHaveValue('')
    expect(screen.getByRole('button', { name: 'Cadastrar ponto' })).toBeInTheDocument()
  })

  it('desativa o ponto e recarrega a lista', async () => {
    await abrir()

    await userEvent.click(screen.getByRole('button', { name: 'Desativar' }))

    await waitFor(() => expect(desativarPonto).toHaveBeenCalledWith('p1'))
    expect(listarPontos).toHaveBeenCalledTimes(2)
  })
})

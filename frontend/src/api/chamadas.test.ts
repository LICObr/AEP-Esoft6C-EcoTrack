import { beforeEach, describe, expect, it, vi } from 'vitest'
import { requisitar } from './cliente'
import { buscarDashboard } from './dashboard'
import { listarHistorico, registrarDescarte } from './descartes'
import { atualizarPonto, cadastrarPonto, desativarPonto, listarCidades, listarPontos } from './pontosColeta'
import type { Endereco } from './tipos'
import { cadastrarUsuario, listarUsuarios } from './usuarios'

vi.mock('./cliente')

const endereco: Endereco = { rua: 'Av. Brasil', numero: '1200', bairro: 'Zona 01', cidade: 'Maringa', estado: 'PR' }

beforeEach(() => {
  vi.mocked(requisitar).mockReset()
  vi.mocked(requisitar).mockResolvedValue(undefined as never)
})

describe('chamadas da api', () => {
  it('dashboard busca o resumo', async () => {
    await buscarDashboard()

    expect(requisitar).toHaveBeenCalledWith('/api/dashboard')
  })

  it('descartes: registra com POST e busca o historico do usuario', async () => {
    await registrarDescarte('u1', 'p1', [{ tipo: 'PILHAS', quantidade: 5, unidade: 'UNIDADE' }])
    await listarHistorico('u1')

    expect(requisitar).toHaveBeenNthCalledWith(1, '/api/descartes', {
      method: 'POST',
      body: JSON.stringify({
        usuarioId: 'u1',
        pontoColetaId: 'p1',
        materiais: [{ tipo: 'PILHAS', quantidade: 5, unidade: 'UNIDADE' }],
      }),
    })
    expect(requisitar).toHaveBeenNthCalledWith(2, '/api/descartes/usuario/u1')
  })

  it('usuarios: lista e cadastra com POST', async () => {
    await listarUsuarios()
    await cadastrarUsuario('Joao', 'joao@email.com')

    expect(requisitar).toHaveBeenNthCalledWith(1, '/api/usuarios')
    expect(requisitar).toHaveBeenNthCalledWith(2, '/api/usuarios', {
      method: 'POST',
      body: JSON.stringify({ nome: 'Joao', email: 'joao@email.com' }),
    })
  })

  it.each([
    [undefined, undefined, '/api/pontos'],
    ['VIDRO', undefined, '/api/pontos?material=VIDRO'],
    [undefined, 'Sarandi', '/api/pontos?cidade=Sarandi'],
    ['VIDRO', 'Maringa', '/api/pontos?material=VIDRO&cidade=Maringa'],
  ] as const)('pontos: listar com material %s e cidade %s chama %s', async (material, cidade, caminho) => {
    await listarPontos(material, cidade)

    expect(requisitar).toHaveBeenCalledWith(caminho)
  })

  it('pontos: lista as cidades, cadastra, atualiza e desativa', async () => {
    await listarCidades()
    await cadastrarPonto('Eco Ponto', endereco, ['PILHAS'])
    await atualizarPonto('p1', 'Eco Ponto Novo', endereco, ['VIDRO'])
    await desativarPonto('p1')

    expect(requisitar).toHaveBeenNthCalledWith(1, '/api/pontos/cidades')
    expect(requisitar).toHaveBeenNthCalledWith(2, '/api/pontos', {
      method: 'POST',
      body: JSON.stringify({ nome: 'Eco Ponto', endereco, materiaisAceitos: ['PILHAS'] }),
    })
    expect(requisitar).toHaveBeenNthCalledWith(3, '/api/pontos/p1', {
      method: 'PUT',
      body: JSON.stringify({ nome: 'Eco Ponto Novo', endereco, materiaisAceitos: ['VIDRO'] }),
    })
    expect(requisitar).toHaveBeenNthCalledWith(4, '/api/pontos/p1', { method: 'DELETE' })
  })
})

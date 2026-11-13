import { afterEach, describe, expect, it, vi } from 'vitest'
import { ErroDeRequisicao, requisitar } from './cliente'

function responder(status: number, corpo?: unknown) {
  const fetchFalso = vi.fn().mockResolvedValue({ ok: status < 400, status, json: async () => corpo })
  vi.stubGlobal('fetch', fetchFalso)
  return fetchFalso
}

afterEach(() => vi.unstubAllGlobals())

describe('requisitar', () => {
  it('devolve o json quando a api responde com sucesso', async () => {
    responder(200, { id: '1' })

    await expect(requisitar('/api/x')).resolves.toEqual({ id: '1' })
  })

  it('manda o content-type json e repassa as opcoes da chamada', async () => {
    const fetchFalso = responder(200, {})

    await requisitar('/api/x', { method: 'POST', body: '{}' })

    const [url, opcoes] = fetchFalso.mock.calls[0]
    expect(url).toContain('/api/x')
    expect(opcoes).toMatchObject({
      method: 'POST',
      body: '{}',
      headers: { 'Content-Type': 'application/json' },
    })
  })

  it('devolve undefined no 204, que nao tem corpo', async () => {
    responder(204)

    await expect(requisitar('/api/x', { method: 'DELETE' })).resolves.toBeUndefined()
  })

  it('lanca ErroDeRequisicao com o status e a mensagem que a api mandou', async () => {
    responder(422, { mensagem: 'O ponto de coleta nao aceita o material OLEO.' })

    const erro = await requisitar('/api/x').catch((e: unknown) => e)

    expect(erro).toBeInstanceOf(ErroDeRequisicao)
    expect(erro).toMatchObject({ status: 422, message: 'O ponto de coleta nao aceita o material OLEO.' })
  })
})

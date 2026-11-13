# Changelog

Formato baseado no [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/). Cada item aponta pro PR onde entrou.

## [2.0.2] — quadro de tarefas

### Adicionado

- Link do quadro de tarefas e resumo de como o time trabalha no README

## [2.0.1] — ajustes finais da entrega 2

### Adicionado

- Testes dos controllers que faltavam: usuário por id, ponto por id e atualização, e `/api/health` com Mongo no ar e fora. Os controllers vão a 100% das linhas (#29)
- Lint do front no CI (#30)

### Alterado

- Lista de materiais e unidades numa fonte única no front, com os tipos derivados dela. Antes estava copiada em 3 telas (#28)
- Histórico e tela de pontos sem `setState` dentro de `useEffect`, como o lint apontava (#30)

## [2.0.0] — entrega 2

### Adicionado

- Dashboard: `GET /api/dashboard` com o total de descartes e a quantidade por material e unidade, e a tela no front (#4, #9)
- Filtro de pontos por cidade e `GET /api/pontos/cidades` (#3, #10)
- Tela de pontos com select de cidade (#10)
- Edição de ponto de coleta na tela de admin (#10)
- Descarte guarda `usuarioNome` e `pontoColetaNome` (Extended Reference) e o histórico mostra o ponto (#2, #10)
- Validação de esquema (`$jsonSchema`) nas três coleções (#6)
- Testes de repositório, índices e esquema contra um MongoDB real com Testcontainers (#4, #5, #6)
- Testes das telas com Vitest e Testing Library (#11)
- Job de front no CI: `npm test` e `npm run build` (#11)
- Documentação: produto, modelagem NoSQL, decisões (ADRs), catálogo da API e manual do usuário (#7, #8, #12)

### Alterado

- Índices compostos de verdade: `descartes` por `usuarioId` + `data`, `pontos_coleta` por `ativo` + `materiaisAceitos` e por `ativo` + `endereco.cidade` (#2, #3)
- Agregação do dashboard soma por material e por unidade, sem juntar kg com litro (#4)
- Erro 400 de corpo inválido (JSON quebrado ou material que não existe) volta no mesmo formato dos outros erros (#10)
- README reescrito com a modelagem e os números reais (referências são `string`, não `ObjectId`)

### Corrigido

- Os índices da entrega 1 nunca eram criados no banco: faltava `auto-index-creation` no `application.yml`. Agora são criados na subida (#2)
- Testcontainers atualizado para 1.21.4, porque o Docker Engine 29 recusa a versão antiga (#4)

## [1.0.0] — entrega 1

Primeira versão: cadastro de usuários e pontos, filtro por material, registro de descarte com validação, histórico por usuário e interface com 4 telas.

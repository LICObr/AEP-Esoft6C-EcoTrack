# API do EcoTrack

Base: `http://localhost:8080`. Tudo em JSON. Documentação interativa: `/swagger-ui.html`.

## Formato de erro

Todo erro volta no mesmo formato:

```json
{
  "timestamp": "2026-11-12T10:15:30.123",
  "status": 422,
  "erro": "Unprocessable Entity",
  "mensagem": "O ponto de coleta nao aceita o material OLEO.",
  "caminho": "/api/descartes"
}
```

| Status | Quando acontece |
|---|---|
| 400 | Dado inválido: campo obrigatório vazio, e-mail inválido, material ou unidade que não existe, parâmetro inválido |
| 404 | Usuário ou ponto de coleta que não existe |
| 409 | E-mail já cadastrado |
| 422 | Regra de negócio: ponto inativo ou material que o ponto não aceita |

Materiais: `PILHAS`, `ELETRONICOS`, `OLEO`, `PLASTICO`, `PAPEL`, `VIDRO`, `METAL`. Unidades: `UNIDADE`, `KG`, `LITRO`.

## Usuários

| Método | Rota | Descrição | Respostas |
|---|---|---|---|
| POST | `/api/usuarios` | Cadastra usuário (e-mail único) | 201, 400, 409 |
| GET | `/api/usuarios` | Lista todos | 200 |
| GET | `/api/usuarios/{id}` | Busca por id | 200, 404 |

Exemplo de cadastro:

```json
{ "nome": "Joao Silva", "email": "joao@email.com" }
```

Resposta 201:

```json
{ "id": "6aa2969751b1fb5d380f1fb2", "nome": "Joao Silva", "email": "joao@email.com" }
```

## Pontos de coleta

| Método | Rota | Descrição | Respostas |
|---|---|---|---|
| POST | `/api/pontos` | Cadastra ponto | 201, 400 |
| GET | `/api/pontos` | Lista pontos **ativos**. Filtros opcionais: `?material=PILHAS` e `?cidade=Maringa` (juntos ou separados) | 200, 400 |
| GET | `/api/pontos/cidades` | Cidades que têm ponto ativo, em ordem alfabética | 200 |
| GET | `/api/pontos/{id}` | Busca por id | 200, 404 |
| PUT | `/api/pontos/{id}` | Atualiza nome, endereço e materiais | 200, 400, 404 |
| DELETE | `/api/pontos/{id}` | Desativa o ponto (não apaga) | 204, 404 |

A cidade é busca exata (com maiúscula e acento como foi cadastrada). Por isso o front monta o filtro a partir de `/api/pontos/cidades`.

Exemplo de cadastro (também serve para o PUT):

```json
{
  "nome": "Eco Ponto Centro",
  "endereco": { "rua": "Av. Brasil", "numero": "1200", "bairro": "Zona 01", "cidade": "Maringa", "estado": "PR" },
  "materiaisAceitos": ["PILHAS", "ELETRONICOS", "PLASTICO"]
}
```

Resposta 201:

```json
{
  "id": "6aa2969751b1fb5d380f1fb5",
  "nome": "Eco Ponto Centro",
  "endereco": { "rua": "Av. Brasil", "numero": "1200", "bairro": "Zona 01", "cidade": "Maringa", "estado": "PR" },
  "materiaisAceitos": ["PILHAS", "ELETRONICOS", "PLASTICO"],
  "ativo": true
}
```

`GET /api/pontos/cidades`:

```json
["Maringa", "Paicandu", "Sarandi"]
```

## Descartes

| Método | Rota | Descrição | Respostas |
|---|---|---|---|
| POST | `/api/descartes` | Registra descarte | 201, 400, 404, 422 |
| GET | `/api/descartes/usuario/{usuarioId}` | Histórico do usuário, do mais recente para o mais antigo | 200, 404 |

Exemplo de registro:

```json
{
  "usuarioId": "6aa2969751b1fb5d380f1fb2",
  "pontoColetaId": "6aa2969751b1fb5d380f1fb5",
  "materiais": [ { "tipo": "PILHAS", "quantidade": 10, "unidade": "UNIDADE" } ]
}
```

Resposta 201 (o nome do usuário e do ponto são copiados na hora do descarte):

```json
{
  "id": "6aa29e5151b1fb5d380f1fc3",
  "usuarioId": "6aa2969751b1fb5d380f1fb2",
  "usuarioNome": "Joao Silva",
  "pontoColetaId": "6aa2969751b1fb5d380f1fb5",
  "pontoColetaNome": "Eco Ponto Centro",
  "materiais": [ { "tipo": "PILHAS", "quantidade": 10, "unidade": "UNIDADE" } ],
  "data": "2026-11-12T10:15:30.123"
}
```

Descartes gravados antes da entrega 2 não têm `usuarioNome` nem `pontoColetaNome` (vêm `null`).

## Dashboard

| Método | Rota | Descrição | Respostas |
|---|---|---|---|
| GET | `/api/dashboard` | Total de descartes e total por material e unidade | 200 |

```json
{
  "totalDescartes": 10,
  "porMaterial": [
    { "tipo": "ELETRONICOS", "unidade": "UNIDADE", "quantidade": 1, "descartes": 1 },
    { "tipo": "METAL", "unidade": "KG", "quantidade": 3, "descartes": 1 },
    { "tipo": "OLEO", "unidade": "LITRO", "quantidade": 7, "descartes": 2 }
  ]
}
```

A quantidade é somada **por unidade**: não faz sentido juntar kg com litro.

## Saúde

`GET /api/health` devolve o estado da API e do Mongo:

```json
{ "api": "UP", "mongo": "UP" }
```

# Arquitetura

## Camadas

```mermaid
flowchart TB
    subgraph Frontend
        A[React + Vite]
    end
    subgraph Backend
        B[Controller] --> C[Service]
        C --> D[Repository]
    end
    A -->|HTTP/JSON| B
    D --> E[(MongoDB)]
```

- **Controller**: recebe DTO validado, chama service, devolve resposta. Nenhuma regra de
  negócio.
- **Service**: concentra toda a regra de negócio, orquestra repositórios e entidades.
- **Domain (entidades)**: construtor validador + métodos de comportamento
  (`aceita`, `desativar`, `validarContra`).
- **Repository**: interfaces `MongoRepository`, sem implementação manual.
- **GlobalExceptionHandler**: única classe que traduz exceção de domínio em código HTTP.

## Fluxo de uma requisição (exemplo: registrar descarte)

```mermaid
sequenceDiagram
    participant F as Frontend
    participant C as DescarteController
    participant S as DescarteService
    participant U as UsuarioService
    participant P as PontoColetaService
    participant R as DescarteRepository
    participant M as MongoDB

    F->>C: POST /api/descartes
    C->>S: registrar(usuarioId, pontoColetaId, materiais)
    S->>U: buscarPorId(usuarioId)
    U-->>S: Usuario
    S->>P: buscarPorId(pontoColetaId)
    P-->>S: PontoColeta
    S->>S: descarte.validarContra(ponto)
    S->>R: save(descarte)
    R->>M: insert
    M-->>R: documento salvo
    R-->>S: Descarte
    S-->>C: Descarte
    C-->>F: 201 Created
```

Se `validarContra` lançar `RegraDeNegocioException` (ponto inativo ou material não aceito), o
`GlobalExceptionHandler` intercepta e devolve `422 Unprocessable Entity` com corpo padronizado
(`timestamp`, `status`, `erro`, `mensagem`, `caminho`).

## Por que essas decisões

- **Sem MapStruct**: os mappers são métodos estáticos simples porque o número de campos é
  pequeno — uma biblioteca de mapeamento adicionaria complexidade sem ganho real aqui.
- **Sem `@DBRef`**: referências entre coleções são `String` (o id), não referência nativa do
  Mongo. Isso evita que o Spring Data busque automaticamente documentos relacionados quando não
  é necessário (custo de performance) e mantém o controle explícito de quando buscar cada
  entidade (via os services).
- **Services concentram a regra, não os controllers**: permite testar toda a regra de negócio
  com Mockito, sem precisar subir o contexto web inteiro.

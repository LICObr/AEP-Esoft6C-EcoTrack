# Decisões de arquitetura

## ADR 0001 — MongoDB como banco

**Status:** aceita.
**Contexto:** a disciplina pede banco NoSQL, e os dados do projeto têm partes que são lidas sempre juntas (endereço do ponto, itens do descarte).
**Decisão:** usar MongoDB, modelando por documento.
**Consequências:** o modelo é pensado pelos padrões de acesso, não por tabelas. Em troca, não há join nem integridade referencial: ela fica na aplicação.

## ADR 0002 — Referência por id e cópia dos nomes no descarte

**Status:** aceita.
**Contexto:** o descarte liga um usuário a um ponto. Embutir o descarte no usuário faria um array crescer sem limite, e `@DBRef` busca documentos sem a gente controlar.
**Decisão:** o descarte guarda os ids como texto e **uma cópia dos nomes** na hora do registro (*extended reference*).
**Consequências:** o histórico lê uma coleção só e não muda se o cadastro mudar. O custo é a duplicação do nome, aceita porque é um registro histórico.

## ADR 0003 — Índices compostos criados pela aplicação

**Status:** aceita.
**Contexto:** na entrega 1 os índices estavam só documentados: o `@Indexed` é ignorado sem `auto-index-creation`, e o banco ficou só com o `_id`.
**Decisão:** ligar `spring.data.mongodb.auto-index-creation` e declarar os índices compostos nas entidades, seguindo a regra ESR.
**Consequências:** índices passam a existir de verdade e são conferidos nos testes com `explain`. A busca por cidade é exata para poder usar o índice.

## ADR 0004 — Validação de esquema também no banco

**Status:** aceita.
**Contexto:** o Java valida, mas qualquer coisa gravada fora da aplicação escaparia.
**Decisão:** aplicar `$jsonSchema` nas três coleções ao subir a aplicação, com `validationLevel: moderate` e `validationAction: error`.
**Consequências:** o banco recusa documento fora do padrão. O nível `moderate` mantém documentos antigos sem quebrar a atualização deles.

## ADR 0005 — Testes de repositório com Mongo real (Testcontainers)

**Status:** aceita.
**Contexto:** mock de repositório não prova que índice, agregação e `distinct` funcionam.
**Decisão:** testes de integração com Testcontainers e `mongo:7`, com um container compartilhado entre as classes.
**Consequências:** os testes precisam de Docker (o CI do GitHub tem). O Testcontainers fica na 1.21.4 porque a 1.19, que vem com o Spring Boot 3.3, não funciona com o Docker Engine 29.

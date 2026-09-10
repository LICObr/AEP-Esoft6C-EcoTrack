# Roteiro do vídeo — Primeira Entrega (2 a 3 minutos)

| Tempo | Conteúdo | Quem |
|---|---|---|
| 0:00–0:20 | Equipe, nome EcoTrack, uma frase do problema | Danilo |
| 0:20–0:45 | ODS 12 / meta 12.5 e como a solução contribui | Danilo |
| 0:45–1:15 | Arquitetura (diagrama do README) e as 3 coleções no MongoDB Compass; citar POO e NoSQL como disciplinas | Thales |
| 1:15–2:15 | Fluxo principal ao vivo no front: filtrar PILHAS → registrar descarte → histórico → tentar material não aceito e mostrar o 422 | Pedro |
| 2:15–2:45 | `./mvnw verify` rodando, JaCoCo ≥ 70%, repositório, Projects, tag; citar Testes de Software | Thales |
| 2:45–3:00 | O que vem na entrega 2 | Pedro |

**Plano B:** se o front falhar durante a gravação, demonstrar pelo Swagger UI
(`http://localhost:8080/swagger-ui.html`).

## Checklist antes de gravar

- [ ] `docker compose up -d` rodando.
- [ ] Backend no ar com `-Dspring-boot.run.profiles=seed` (garante dados de exemplo).
- [ ] Frontend no ar (`npm run dev`).
- [ ] `./mvnw verify` já rodado uma vez, com o relatório do JaCoCo aberto numa aba.
- [ ] MongoDB Compass conectado, com as 3 coleções visíveis.
- [ ] Testar o fluxo de erro 422 uma vez antes de gravar (registrar um material que o ponto
      escolhido não aceita), pra saber exatamente o que vai aparecer na tela.

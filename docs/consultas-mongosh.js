// consultas do ecotrack para rodar no mongosh:
// mongosh "mongodb://localhost:27017/ecotrack" --file docs/consultas-mongosh.js

print("contagem: usuarios=" + db.usuarios.countDocuments()
  + " pontos=" + db.pontos_coleta.countDocuments()
  + " descartes=" + db.descartes.countDocuments());

print("\n== indices ==");
["usuarios", "pontos_coleta", "descartes"].forEach((colecao) => {
  print(colecao + ": " + db.getCollection(colecao).getIndexes().map((i) => i.name).join(", "));
});

// resumo do plano de execucao de uma consulta
function resumo(titulo, explicacao) {
  const plano = JSON.stringify(explicacao.queryPlanner.winningPlan);
  const indice = (plano.match(/"indexName":"([^"]+)"/) || [])[1];
  const stats = explicacao.executionStats;
  print(titulo + ": " + (plano.includes("IXSCAN") ? "IXSCAN" : "COLLSCAN")
    + " | indice=" + indice
    + " | sort_em_memoria=" + plano.includes('"stage":"SORT"')
    + " | examinados=" + stats.totalDocsExamined
    + " | retornados=" + stats.nReturned);
}

print("\n== explain ==");
const usuarioId = db.usuarios.findOne({ nome: "Joao Silva" })._id.toString();
resumo("historico do usuario",
  db.descartes.find({ usuarioId: usuarioId }).sort({ data: -1 }).explain("executionStats"));
resumo("pontos ativos com PILHAS",
  db.pontos_coleta.find({ ativo: true, materiaisAceitos: "PILHAS" }).explain("executionStats"));
resumo("pontos ativos em Sarandi",
  db.pontos_coleta.find({ ativo: true, "endereco.cidade": "Sarandi" }).explain("executionStats"));

print("\n== cidades com ponto ativo ==");
printjson(db.pontos_coleta.distinct("endereco.cidade", { ativo: true }));

print("\n== total por material e unidade ==");
printjson(db.descartes.aggregate([
  { $unwind: "$materiais" },
  { $group: {
      _id: { tipo: "$materiais.tipo", unidade: "$materiais.unidade" },
      quantidade: { $sum: "$materiais.quantidade" },
      descartes: { $sum: 1 }
  } },
  { $sort: { "_id.tipo": 1, "_id.unidade": 1 } }
]).toArray());

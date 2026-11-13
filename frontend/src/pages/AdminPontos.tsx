import { useEffect, useState } from "react";
import { atualizarPonto, cadastrarPonto, desativarPonto, listarPontos } from "../api/pontosColeta";
import { MATERIAIS } from "../api/constantes";
import type { Material, PontoColeta } from "../api/tipos";

export function AdminPontos() {
  const [pontos, setPontos] = useState<PontoColeta[]>([]);
  const [editandoId, setEditandoId] = useState<string | null>(null);
  const [nome, setNome] = useState("");
  const [rua, setRua] = useState("");
  const [numero, setNumero] = useState("");
  const [bairro, setBairro] = useState("");
  const [cidade, setCidade] = useState("Maringá");
  const [estado, setEstado] = useState("PR");
  const [materiais, setMateriais] = useState<Material[]>([]);

  function carregar() {
    listarPontos().then(setPontos);
  }

  useEffect(() => {
    carregar();
  }, []);

  function alternarMaterial(material: Material) {
    setMateriais((atual) =>
      atual.includes(material) ? atual.filter((m) => m !== material) : [...atual, material]
    );
  }

  function limparFormulario() {
    setEditandoId(null);
    setNome("");
    setRua("");
    setNumero("");
    setBairro("");
    setMateriais([]);
  }

  // joga os dados do ponto no formulario pra editar
  function iniciarEdicao(ponto: PontoColeta) {
    setEditandoId(ponto.id);
    setNome(ponto.nome);
    setRua(ponto.endereco.rua);
    setNumero(ponto.endereco.numero ?? "");
    setBairro(ponto.endereco.bairro ?? "");
    setCidade(ponto.endereco.cidade);
    setEstado(ponto.endereco.estado);
    setMateriais(ponto.materiaisAceitos);
  }

  async function salvar(evento: React.FormEvent) {
    evento.preventDefault();
    const endereco = { rua, numero, bairro, cidade, estado };

    if (editandoId) {
      await atualizarPonto(editandoId, nome, endereco, materiais);
    } else {
      await cadastrarPonto(nome, endereco, materiais);
    }

    limparFormulario();
    carregar();
  }

  async function desativar(id: string) {
    await desativarPonto(id);
    carregar();
  }

  return (
    <section>
      <h2>Admin de pontos</h2>

      <form onSubmit={salvar}>
        <label htmlFor="nome-ponto">Nome</label>
        <input id="nome-ponto" value={nome} onChange={(e) => setNome(e.target.value)} required />

        <label htmlFor="rua-ponto">Rua</label>
        <input id="rua-ponto" value={rua} onChange={(e) => setRua(e.target.value)} required />

        <label htmlFor="numero-ponto">Número</label>
        <input id="numero-ponto" value={numero} onChange={(e) => setNumero(e.target.value)} />

        <label htmlFor="bairro-ponto">Bairro</label>
        <input id="bairro-ponto" value={bairro} onChange={(e) => setBairro(e.target.value)} />

        <label htmlFor="cidade-ponto">Cidade</label>
        <input id="cidade-ponto" value={cidade} onChange={(e) => setCidade(e.target.value)} required />

        <label htmlFor="estado-ponto">Estado</label>
        <input id="estado-ponto" value={estado} onChange={(e) => setEstado(e.target.value)} required />

        <fieldset>
          <legend>Materiais aceitos</legend>
          {MATERIAIS.map((material) => (
            <label key={material}>
              <input
                type="checkbox"
                checked={materiais.includes(material)}
                onChange={() => alternarMaterial(material)}
              />
              {material}
            </label>
          ))}
        </fieldset>

        <button type="submit">{editandoId ? "Salvar alterações" : "Cadastrar ponto"}</button>
        {editandoId && (
          <button type="button" onClick={limparFormulario}>Cancelar edição</button>
        )}
      </form>

      <ul>
        {pontos.map((ponto) => (
          <li key={ponto.id}>
            {ponto.nome} — {ponto.endereco.cidade}{" "}
            <button onClick={() => iniciarEdicao(ponto)}>Editar</button>{" "}
            <button onClick={() => desativar(ponto.id)}>Desativar</button>
          </li>
        ))}
      </ul>
    </section>
  );
}

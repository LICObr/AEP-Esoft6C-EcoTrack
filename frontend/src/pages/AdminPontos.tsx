import { useEffect, useState } from "react";
import { atualizarPonto, cadastrarPonto, desativarPonto, listarPontos } from "../api/pontosColeta";
import { MATERIAIS } from "../api/constantes";
import type { Material, PontoColeta } from "../api/tipos";
import { TituloPagina } from "../components/TituloPagina";

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
    <section className="pagina">
      <TituloPagina
        etiqueta="Administração"
        titulo="Admin de pontos"
        descricao="Cadastre, edite e desative os pontos de coleta que aparecem para todo mundo."
      />

      <div className="admin">
        <form className="formulario cartao" onSubmit={salvar}>
          <div className="grade-campos">
            <div className="campo campo--cheio">
              <label htmlFor="nome-ponto">Nome</label>
              <input id="nome-ponto" value={nome} onChange={(e) => setNome(e.target.value)} required />
            </div>

            <div className="campo">
              <label htmlFor="rua-ponto">Rua</label>
              <input id="rua-ponto" value={rua} onChange={(e) => setRua(e.target.value)} required />
            </div>

            <div className="campo">
              <label htmlFor="numero-ponto">Número</label>
              <input id="numero-ponto" value={numero} onChange={(e) => setNumero(e.target.value)} />
            </div>

            <div className="campo">
              <label htmlFor="bairro-ponto">Bairro</label>
              <input id="bairro-ponto" value={bairro} onChange={(e) => setBairro(e.target.value)} />
            </div>

            <div className="campo">
              <label htmlFor="cidade-ponto">Cidade</label>
              <input id="cidade-ponto" value={cidade} onChange={(e) => setCidade(e.target.value)} required />
            </div>

            <div className="campo">
              <label htmlFor="estado-ponto">Estado</label>
              <input id="estado-ponto" value={estado} onChange={(e) => setEstado(e.target.value)} required />
            </div>
          </div>

          <fieldset className="opcoes">
            <legend>Materiais aceitos</legend>
            <div className="opcoes__lista">
              {MATERIAIS.map((material) => (
                <label key={material} className="opcao">
                  <input
                    type="checkbox"
                    checked={materiais.includes(material)}
                    onChange={() => alternarMaterial(material)}
                  />
                  {material}
                </label>
              ))}
            </div>
          </fieldset>

          <div className="formulario__acoes">
            <button type="submit" className="botao">
              {editandoId ? "Salvar alterações" : "Cadastrar ponto"}
            </button>
            {editandoId && (
              <button type="button" className="botao botao--secundario" onClick={limparFormulario}>
                Cancelar edição
              </button>
            )}
          </div>
        </form>

        <ul className="lista-admin">
          {pontos.map((ponto) => (
            <li
              key={ponto.id}
              className={ponto.id === editandoId ? "ponto-admin ponto-admin--editando cartao" : "ponto-admin cartao"}
            >
              <div className="ponto-admin__texto">
                <strong>{ponto.nome}</strong>
                <span>{ponto.endereco.cidade}</span>
              </div>
              <div className="ponto-admin__acoes">
                <button className="botao botao--secundario botao--pequeno" onClick={() => iniciarEdicao(ponto)}>
                  Editar
                </button>
                <button className="botao botao--perigo botao--pequeno" onClick={() => desativar(ponto.id)}>
                  Desativar
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </section>
  );
}

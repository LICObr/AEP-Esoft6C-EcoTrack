package br.com.ecotrack.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ecotrack.domain.model.Descarte;
import br.com.ecotrack.domain.model.ItemDescarte;
import br.com.ecotrack.domain.model.PontoColeta;
import br.com.ecotrack.domain.model.Usuario;
import br.com.ecotrack.repository.DescarteRepository;

@Service
public class DescarteService {

    private final DescarteRepository descarteRepository;
    private final UsuarioService usuarioService;
    private final PontoColetaService pontoColetaService;

    public DescarteService(DescarteRepository descarteRepository,
                            UsuarioService usuarioService,
                            PontoColetaService pontoColetaService) {
        this.descarteRepository = descarteRepository;
        this.usuarioService = usuarioService;
        this.pontoColetaService = pontoColetaService;
    }

    public Descarte registrar(String usuarioId, String pontoColetaId, List<ItemDescarte> materiais) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        PontoColeta ponto = pontoColetaService.buscarPorId(pontoColetaId);

        Descarte descarte = new Descarte(usuario.getId(), ponto.getId(), materiais);
        descarte.validarContra(ponto);

        return descarteRepository.save(descarte);
    }

    public List<Descarte> listarHistoricoDoUsuario(String usuarioId) {
        usuarioService.buscarPorId(usuarioId);
        return descarteRepository.findByUsuarioIdOrderByDataDesc(usuarioId);
    }
}

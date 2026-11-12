package br.com.ecotrack.service;

import org.springframework.stereotype.Service;

import br.com.ecotrack.domain.model.Dashboard;
import br.com.ecotrack.repository.DescarteRepository;

@Service
public class DashboardService {

    private final DescarteRepository descarteRepository;

    public DashboardService(DescarteRepository descarteRepository) {
        this.descarteRepository = descarteRepository;
    }

    public Dashboard montar() {
        return new Dashboard(descarteRepository.count(), descarteRepository.totalPorMaterial());
    }
}

package br.com.ecotrack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ecotrack.dto.DashboardResponse;
import br.com.ecotrack.mapper.DashboardMapper;
import br.com.ecotrack.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> montar() {
        return ResponseEntity.ok(DashboardMapper.paraResposta(dashboardService.montar()));
    }
}

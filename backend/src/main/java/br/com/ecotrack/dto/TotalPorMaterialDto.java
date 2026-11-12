package br.com.ecotrack.dto;

import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.Unidade;

public record TotalPorMaterialDto(Material tipo, Unidade unidade, long quantidade, long descartes) {
}

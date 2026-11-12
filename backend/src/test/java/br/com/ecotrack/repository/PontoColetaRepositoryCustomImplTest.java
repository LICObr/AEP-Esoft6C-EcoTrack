package br.com.ecotrack.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import br.com.ecotrack.domain.model.PontoColeta;

@ExtendWith(MockitoExtension.class)
class PontoColetaRepositoryCustomImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private PontoColetaRepositoryCustomImpl repository;

    @Test
    void deveDevolverAsCidadesEmOrdemAlfabetica() {
        when(mongoTemplate.findDistinct(any(Query.class), eq("endereco.cidade"),
                eq(PontoColeta.class), eq(String.class)))
                .thenReturn(List.of("Sarandi", "Maringa", "Paicandu"));

        List<String> cidades = repository.listarCidadesAtivas();

        assertThat(cidades).containsExactly("Maringa", "Paicandu", "Sarandi");
    }
}

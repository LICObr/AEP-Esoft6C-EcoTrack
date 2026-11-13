package br.com.ecotrack.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;

import com.mongodb.client.MongoDatabase;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MongoTemplate mongoTemplate;

    @Test
    void deveDizerQueOMongoEstaNoArQuandoOPingFunciona() throws Exception {
        when(mongoTemplate.getDb()).thenReturn(mock(MongoDatabase.class));

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.api", is("UP")))
                .andExpect(jsonPath("$.mongo", is("UP")));
    }

    @Test
    void deveDizerQueOMongoCaiuQuandoOPingFalha() throws Exception {
        when(mongoTemplate.getDb()).thenThrow(new IllegalStateException("sem conexao"));

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.api", is("UP")))
                .andExpect(jsonPath("$.mongo", is("DOWN")));
    }
}

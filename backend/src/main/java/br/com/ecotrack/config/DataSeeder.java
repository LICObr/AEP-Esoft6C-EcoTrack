package br.com.ecotrack.config;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.ecotrack.domain.model.Descarte;
import br.com.ecotrack.domain.model.Endereco;
import br.com.ecotrack.domain.model.ItemDescarte;
import br.com.ecotrack.domain.model.Material;
import br.com.ecotrack.domain.model.PontoColeta;
import br.com.ecotrack.domain.model.Unidade;
import br.com.ecotrack.domain.model.Usuario;
import br.com.ecotrack.repository.DescarteRepository;
import br.com.ecotrack.repository.PontoColetaRepository;
import br.com.ecotrack.repository.UsuarioRepository;

@Configuration
@Profile("seed")
public class DataSeeder {

    @Bean
    public CommandLineRunner semear(UsuarioRepository usuarioRepository,
                                     PontoColetaRepository pontoColetaRepository,
                                     DescarteRepository descarteRepository) {
        return args -> {
            if (usuarioRepository.count() > 0) {
                return;
            }

            Usuario joao = usuarioRepository.save(new Usuario("Joao Silva", "joao@email.com"));
            Usuario maria = usuarioRepository.save(new Usuario("Maria Souza", "maria@email.com"));
            Usuario pedro = usuarioRepository.save(new Usuario("Pedro Santos", "pedro@email.com"));

            PontoColeta centro = pontoColetaRepository.save(new PontoColeta("Eco Ponto Centro",
                    new Endereco("Av. Brasil", "1200", "Zona 01", "Maringa", "PR"),
                    Set.of(Material.PILHAS, Material.ELETRONICOS, Material.PLASTICO)));

            PontoColeta zonaSete = pontoColetaRepository.save(new PontoColeta("Eco Ponto Zona 7",
                    new Endereco("Rua Pioneiro Jose Assalin", "300", "Zona 07", "Maringa", "PR"),
                    Set.of(Material.VIDRO, Material.METAL, Material.PAPEL)));

            PontoColeta novoCentro = pontoColetaRepository.save(new PontoColeta("Eco Ponto Novo Centro",
                    new Endereco("Av. Colombo", "5790", "Jardim Universitario", "Maringa", "PR"),
                    Set.of(Material.OLEO, Material.PLASTICO, Material.PAPEL)));

            PontoColeta parqueDoIngas = pontoColetaRepository.save(new PontoColeta("Eco Ponto Parque do Ingas",
                    new Endereco("Av. Herval", "1000", "Vila Esperanca", "Maringa", "PR"),
                    Set.of(Material.PILHAS, Material.VIDRO)));

            PontoColeta contornoNorte = pontoColetaRepository.save(new PontoColeta("Eco Ponto Contorno Norte",
                    new Endereco("Av. Prudente de Moraes", "800", "Zona 03", "Maringa", "PR"),
                    Set.of(Material.ELETRONICOS, Material.METAL)));

            PontoColeta cidadeAlta = pontoColetaRepository.save(new PontoColeta("Eco Ponto Cidade Alta",
                    new Endereco("Rua Santos Dumont", "450", "Cidade Alta", "Maringa", "PR"),
                    Set.of(Material.PAPEL, Material.PLASTICO, Material.VIDRO)));

            PontoColeta sarandi = pontoColetaRepository.save(new PontoColeta("Eco Ponto Sarandi",
                    new Endereco("Rua das Palmeiras", "210", "Centro", "Sarandi", "PR"),
                    Set.of(Material.PILHAS, Material.PAPEL, Material.METAL)));

            PontoColeta paicandu = pontoColetaRepository.save(new PontoColeta("Eco Ponto Paicandu",
                    new Endereco("Av. dos Ipes", "95", "Jardim Alvorada", "Paicandu", "PR"),
                    Set.of(Material.OLEO, Material.VIDRO, Material.ELETRONICOS)));

            descarteRepository.save(new Descarte(joao.getId(), joao.getNome(), centro.getId(), centro.getNome(),
                    List.of(new ItemDescarte(Material.PILHAS, 10, Unidade.UNIDADE))));
            descarteRepository.save(new Descarte(joao.getId(), joao.getNome(), novoCentro.getId(), novoCentro.getNome(),
                    List.of(new ItemDescarte(Material.OLEO, 2, Unidade.LITRO))));
            descarteRepository.save(new Descarte(maria.getId(), maria.getNome(), zonaSete.getId(), zonaSete.getNome(),
                    List.of(new ItemDescarte(Material.VIDRO, 5, Unidade.KG))));
            descarteRepository.save(new Descarte(maria.getId(), maria.getNome(), centro.getId(), centro.getNome(),
                    List.of(new ItemDescarte(Material.ELETRONICOS, 1, Unidade.UNIDADE))));
            descarteRepository.save(new Descarte(pedro.getId(), pedro.getNome(), parqueDoIngas.getId(), parqueDoIngas.getNome(),
                    List.of(new ItemDescarte(Material.PILHAS, 20, Unidade.UNIDADE))));
            descarteRepository.save(new Descarte(pedro.getId(), pedro.getNome(), contornoNorte.getId(), contornoNorte.getNome(),
                    List.of(new ItemDescarte(Material.METAL, 3, Unidade.KG))));
            descarteRepository.save(new Descarte(joao.getId(), joao.getNome(), cidadeAlta.getId(), cidadeAlta.getNome(),
                    List.of(new ItemDescarte(Material.PAPEL, 8, Unidade.KG))));
            descarteRepository.save(new Descarte(maria.getId(), maria.getNome(), novoCentro.getId(), novoCentro.getNome(),
                    List.of(new ItemDescarte(Material.PLASTICO, 4, Unidade.KG))));
            descarteRepository.save(new Descarte(pedro.getId(), pedro.getNome(), sarandi.getId(), sarandi.getNome(),
                    List.of(new ItemDescarte(Material.PILHAS, 6, Unidade.UNIDADE),
                            new ItemDescarte(Material.PAPEL, 2, Unidade.KG))));
            descarteRepository.save(new Descarte(joao.getId(), joao.getNome(), paicandu.getId(), paicandu.getNome(),
                    List.of(new ItemDescarte(Material.OLEO, 5, Unidade.LITRO))));
        };
    }
}

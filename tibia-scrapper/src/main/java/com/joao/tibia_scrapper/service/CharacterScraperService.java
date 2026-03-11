package com.joao.tibia_scrapper.service;

import com.joao.tibia_scrapper.dto.CharacterDTO;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CharacterScraperService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UsuarioRepository usuarioRepository;

    public CharacterScraperService(RestTemplate restTemplate, ObjectMapper objectMapper,
            UsuarioRepository usuarioRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public CharacterDTO buscarPersonagem(String nomeChar) {
        try {
            String encodedName = URLEncoder.encode(nomeChar, StandardCharsets.UTF_8);
            String url = "https://api.tibiadata.com/v4/character/" + encodedName;

            String jsonResponse = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode characterNode = root.path("character").path("character");

            if (characterNode.isMissingNode() || characterNode.path("name").asText().isEmpty()) {
                return null;
            }

            String name = characterNode.path("name").asText();
            String vocation = characterNode.path("vocation").asText();
            int level = characterNode.path("level").asInt();
            String world = characterNode.path("world").asText();
            String residence = characterNode.path("residence").asText();
            String sex = characterNode.path("sex").asText();

            return new CharacterDTO(name, vocation, level, world, residence, sex);

        } catch (Exception e) {
            log.error("Erro ao buscar personagem '{}' na API TibiaData: {}", nomeChar, e.getMessage(), e);
            return null;
        }
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void atualizarTodosPersonagensVinculados() {
        log.info("Iniciando rotina de atualização automática de personagens vinculados...");
        List<Usuario> usuariosComChar = usuarioRepository.findAllUsuariosComPersonagem();

        int atualizados = 0;

        for (Usuario usuario : usuariosComChar) {
            try {
                CharacterDTO character = buscarPersonagem(usuario.getCharName());

                if (character != null) {
                    usuario.setCharLevel(character.level());
                    usuario.setCharVocation(character.vocation());
                    usuario.setCharWorld(character.world());
                    usuario.setCharResidence(character.residence());

                    usuarioRepository.save(usuario);
                    atualizados++;
                }

                Thread.sleep(1000);

            } catch (Exception e) {
                log.error("Falha ao atualizar personagem automático '{}' (User ID: {}): {}",
                        usuario.getCharName(), usuario.getId(), e.getMessage());
            }
        }

        log.info("Rotina de atualização concluída! {}/{} personagens atualizados.", atualizados,
                usuariosComChar.size());
    }
}
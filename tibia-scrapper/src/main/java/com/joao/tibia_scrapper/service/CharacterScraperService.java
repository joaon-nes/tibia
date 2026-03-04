package com.joao.tibia_scrapper.service;

import com.joao.tibia_scrapper.dto.CharacterDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class CharacterScraperService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CharacterScraperService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
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
}
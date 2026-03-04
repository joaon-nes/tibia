package com.joao.tibia_scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoRestController {

    private final EquipamentoRepository equipamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;

    public EquipamentoRestController(EquipamentoRepository equipamentoRepository,
            UsuarioRepository usuarioRepository,
            ObjectMapper objectMapper) {
        this.equipamentoRepository = equipamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/buscar")
    public List<Equipamento> buscar(@RequestParam String termo, Principal principal) {
        Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        int levelUser = usuario.getCharLevel() != null ? usuario.getCharLevel() : 0;
        String vocUser = usuario.getCharVocation() != null ? usuario.getCharVocation().toLowerCase() : "";

        String vocacaoFiltro = "";
        if (vocUser.contains("knight"))
            vocacaoFiltro = "knight";
        else if (vocUser.contains("paladin"))
            vocacaoFiltro = "paladin";
        else if (vocUser.contains("druid"))
            vocacaoFiltro = "druid";
        else if (vocUser.contains("sorcerer"))
            vocacaoFiltro = "sorcerer";
        else if (vocUser.contains("monk"))
            vocacaoFiltro = "monk";

        return equipamentoRepository.findFiltrado(termo, levelUser, vocacaoFiltro)
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    @PostMapping("/salvar-sets")
    public ResponseEntity<String> salvarSets(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            @SuppressWarnings("unchecked")
            Map<String, Object> sets = (Map<String, Object>) payload.get("sets");
            String setsJson = objectMapper.writeValueAsString(sets);

            usuario.setSetsEquipamentos(setsJson);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Sets salvos com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao salvar sets do usuário '{}': {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao salvar os sets: " + e.getMessage());
        }
    }
}
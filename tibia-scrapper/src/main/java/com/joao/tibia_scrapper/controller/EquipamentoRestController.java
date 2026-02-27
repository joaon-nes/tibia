package com.joao.tibia_scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import com.joao.tibia_scrapper.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoRestController {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/buscar")
    public List<Equipamento> buscar(@RequestParam String termo, Principal principal) {
        Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        int levelUser = usuario.getCharLevel() != null ? usuario.getCharLevel() : 0;
        
        String vocUser = usuario.getCharVocation() != null ? usuario.getCharVocation().toLowerCase() : "";
        
        String vocacaoFiltro = "";
        if (vocUser.contains("knight")) {
            vocacaoFiltro = "knight";
        } else if (vocUser.contains("paladin")) {
            vocacaoFiltro = "paladin";
        } else if (vocUser.contains("druid")) {
            vocacaoFiltro = "druid";
        } else if (vocUser.contains("sorcerer")) {
            vocacaoFiltro = "sorcerer";
        } else if (vocUser.contains("monk")) {
            vocacaoFiltro = "monk";
        }

        List<Equipamento> resultados = equipamentoRepository.findFiltrado(termo, levelUser, vocacaoFiltro);

        return resultados.stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    @PostMapping("/salvar-sets")
    public ResponseEntity<String> salvarSets(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Map<String, Object> sets = (Map<String, Object>) payload.get("sets");
            
            ObjectMapper objectMapper = new ObjectMapper();
            String setsJson = objectMapper.writeValueAsString(sets);
            
            usuario.setSetsEquipamentos(setsJson);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Sets salvos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao salvar os sets: " + e.getMessage());
        }
    }
}
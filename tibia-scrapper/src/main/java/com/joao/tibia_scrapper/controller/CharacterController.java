package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.dto.CharacterDTO;
import com.joao.tibia_scrapper.service.CharacterScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/character")
public class CharacterController {

    @Autowired
    private CharacterScraperService characterService;

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPersonagem(@RequestParam String nome) {
        CharacterDTO character = characterService.buscarPersonagem(nome);
        if (character == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(character);
    }
}
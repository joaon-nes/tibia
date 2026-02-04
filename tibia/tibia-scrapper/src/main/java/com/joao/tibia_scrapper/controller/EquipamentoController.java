package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
@CrossOrigin("*")
public class EquipamentoController {

    @Autowired
    private EquipamentoRepository repository;

    @GetMapping("/buscar")
    public List<Equipamento> buscarEquipamentos(
            @RequestParam String vocacao, 
            @RequestParam(defaultValue = "1000") Integer level) {
        
        return repository.findByVocacoesContainingAndLevelMinimoLessThanEqual(vocacao, level);
    }

    @GetMapping("/categoria/{categoria}")
    public List<Equipamento> buscarPorCategoria(@PathVariable String categoria) {
        return repository.findByCategoria(categoria);
    }

    @GetMapping("/todos")
    public List<Equipamento> listarTodos() {
        return repository.findAll();
    }
}
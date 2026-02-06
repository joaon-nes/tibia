package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    @Autowired
    private EquipamentoRepository repository;

    @GetMapping
    public List<Equipamento> listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String vocacao,
            @RequestParam(required = false) String protecao,
            @RequestParam(required = false) String elemento,
            @RequestParam(required = false) String bonus,
            @RequestParam(required = false) String atributos,
            @RequestParam(required = false) String cargas,
            @RequestParam(required = false) String duracao,
            @RequestParam(required = false) Integer range,
            @RequestParam(required = false) Integer slots,
            @RequestParam(required = false) Integer tier) {

        if (categoria != null && !categoria.isEmpty()) {
            return repository.findComFiltrosAvancados(
                    categoria, level, vocacao, protecao, elemento, 
                    bonus, atributos, cargas, duracao, range, slots, tier
            );
        }

        return repository.findAll();
    }
}
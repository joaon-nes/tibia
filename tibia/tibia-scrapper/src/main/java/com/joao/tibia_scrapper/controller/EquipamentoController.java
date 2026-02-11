package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.dto.EquipamentoFilterDTO;
import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/enciclopedia")
public class EquipamentoController {

    @Autowired
    private EquipamentoRepository repository;

    @GetMapping
    public List<Equipamento> listar(EquipamentoFilterDTO filtro) {

        List<String> categoriasLista = null;
        if (filtro.categoria() != null && !filtro.categoria().isEmpty()) {
            categoriasLista = Arrays.asList(filtro.categoria().split(","));
        }

        return repository.findComFiltrosAvancados(
                categoriasLista, 
                filtro.level(), 
                filtro.vocacao(), 
                filtro.protecao(), 
                filtro.elemento(), 
                filtro.bonus(), 
                filtro.atributos(), 
                filtro.range(), 
                filtro.slots(), 
                filtro.tier()
        );
    }

    @GetMapping("/categoria/{cat}")
    public List<Equipamento> buscarParaBuild(@PathVariable String cat) {
        return repository.findByCategoriaIgnoreCase(cat);
    }
}
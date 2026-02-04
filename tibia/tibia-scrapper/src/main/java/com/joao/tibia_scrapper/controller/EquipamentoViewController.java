package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EquipamentoViewController {

    @Autowired
    private EquipamentoRepository repository;

    @GetMapping("/equipamentos")
    public String listarEquipamentos(@RequestParam(required = false) String categoria, Model model) {
        if (categoria != null && !categoria.isEmpty()) {
            model.addAttribute("itens", repository.findByCategoriaOrderByNomeAsc(categoria));
            model.addAttribute("titulo", categoria);
        } else {
            model.addAttribute("exibirMenu", true);
        }
        return "lista-equipamentos";
    }
}
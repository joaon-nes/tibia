package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.repository.MagiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MagiaViewController {

    @Autowired
    private MagiaRepository magiaRepository;

    @GetMapping("/enciclopedia/magias")
    public String listarMagias(Model model) {
        model.addAttribute("magias", magiaRepository.findAll());
        return "magias";
    }
}
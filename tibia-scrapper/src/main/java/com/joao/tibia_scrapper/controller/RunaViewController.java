package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.repository.RunaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RunaViewController {

    @Autowired
    private RunaRepository runaRepository;

    @GetMapping("/enciclopedia/runas")
    public String listarRunas(Model model) {
        model.addAttribute("runas", runaRepository.findAll());
        return "runas";
    }
}
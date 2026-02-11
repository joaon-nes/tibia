package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.Enums.CategoriaEnum;
import com.joao.tibia_scrapper.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ScraperService scraperService;

    @GetMapping("/importar-tudo")
    public String importarTudo() {
        for (CategoriaEnum cat : CategoriaEnum.values()) {
            try {
                Thread.sleep(1000); 
                scraperService.importarCategoria(cat.getNome(), cat.getSubUrl());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Erro: Processo interrompido.";
            }
        }
        return "Importação de todas as categorias concluída.";
    }
}
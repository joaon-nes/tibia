package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ScraperService scraperService;

    @GetMapping("/importar-tudo")
    public String importarTudo() throws InterruptedException {
        // Equipamentos
        scraperService.importarCategoria("Helmets", "wiki/Capacetes");
        scraperService.importarCategoria("Armors", "wiki/Armaduras");
        scraperService.importarCategoria("Legs", "wiki/Calças");
        scraperService.importarCategoria("Boots", "wiki/Botas");
        scraperService.importarCategoria("Shields", "wiki/Escudos");
        scraperService.importarCategoria("Spellbooks", "wiki/Spellbooks");
        // Armas
        scraperService.importarCategoria("Swords", "wiki/Espadas");
        scraperService.importarCategoria("Axes", "wiki/Machados");
        scraperService.importarCategoria("Clubs", "wiki/Clavas");
        scraperService.importarCategoria("Fist", "wiki/Punhos");
        scraperService.importarCategoria("Wands", "wiki/Wands");
        scraperService.importarCategoria("Rods", "wiki/Rods");
        scraperService.importarCategoria("Distance", "wiki/Distância");
        scraperService.importarCategoria("Ammunition", "wiki/Munição");
        scraperService.importarCategoria("Quivers", "wiki/Aljavas");
        // Acessórios e Utilidades
        scraperService.importarCategoria("Amulets", "wiki/Amuletos_e_Colares");
        scraperService.importarCategoria("Rings", "wiki/An%C3%A9is");
        scraperService.importarCategoria("Extra Slot", "wiki/Extra_Slot");
        scraperService.importarCategoria("Backpacks", "wiki/Recipientes");

        return "Importação concluída.";
    }

    @GetMapping("/importar/{categoria}/{path}")
    public String importarUnica(@PathVariable String categoria, @PathVariable String path) {
        new Thread(() -> scraperService.importarCategoria(categoria, path)).start();
        return "Processando " + categoria + " em segundo plano. Olhe o console!";
    }
}
package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.Enums.CategoriaEnum;
import com.joao.tibia_scrapper.service.ScraperService;
import com.joao.tibia_scrapper.service.TibiaCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ScraperService scraperService;

    @Autowired
    private TibiaCoinService tibiaCoinService;

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

    @GetMapping("/tibia-coin")
    public String sincronizar(RedirectAttributes attributes) {
        try {
            tibiaCoinService.sincronizarTodosOsMundos(TibiaCoinService.LISTA_MUNDOS);

            attributes.addFlashAttribute("mensagem",
                    "Sincronização iniciada para " + TibiaCoinService.LISTA_MUNDOS.size() + " mundos.");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao sincronizar: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
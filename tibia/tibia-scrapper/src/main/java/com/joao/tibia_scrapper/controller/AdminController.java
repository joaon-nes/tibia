package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.service.ScraperService;
import com.joao.tibia_scrapper.service.TibiaCoinService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Arrays;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ScraperService scraperService;

    @Autowired
    private TibiaCoinService tibiaCoinService;

    @GetMapping("/importar-tudo")
    public String importarTudo() throws InterruptedException {
        
        scraperService.importarCategoria("Helmets", "wiki/Capacetes");
        scraperService.importarCategoria("Armors", "wiki/Armaduras");
        scraperService.importarCategoria("Legs", "wiki/Calças");
        scraperService.importarCategoria("Boots", "wiki/Botas");
        scraperService.importarCategoria("Shields", "wiki/Escudos");
        scraperService.importarCategoria("Spellbooks", "wiki/Spellbooks");
        
        scraperService.importarCategoria("Swords", "wiki/Espadas");
        scraperService.importarCategoria("Axes", "wiki/Machados");
        scraperService.importarCategoria("Clubs", "wiki/Clavas");
        scraperService.importarCategoria("Fist", "wiki/Punhos");
        scraperService.importarCategoria("Wands", "wiki/Wands");
        scraperService.importarCategoria("Rods", "wiki/Rods");
        scraperService.importarCategoria("Distance", "wiki/Distância");
        scraperService.importarCategoria("Ammunition", "wiki/Munição");
        scraperService.importarCategoria("Quivers", "wiki/Aljavas");

        scraperService.importarCategoria("Amulets", "wiki/Amuletos_e_Colares");
        scraperService.importarCategoria("Rings", "wiki/An%C3%A9is");
        scraperService.importarCategoria("Extra Slot", "wiki/Extra_Slot");
        scraperService.importarCategoria("Backpacks", "wiki/Recipientes");

        return "Importação concluída.";
    }

    @GetMapping("/tibia-coin")
    public String sincronizar(RedirectAttributes attributes) {
        try {
            List<String> mundosManuais = Arrays.asList(
                    "Aethera", "Ambra", "Antica", "Astera", "Belobra", "Blumera", "Bona", "Bravoria",
                    "Calmera", "Cantabra", "Celebra", "Celesta", "Citra", "Collabra", "Descubra",
                    "Dia", "Divina", "Eclipta", "Epoca", "Escura", "Esmera", "Etebra", "Ferobra",
                    "Fibera", "Firmera", "Flamera", "Gentebra", "Gladera", "Gladibra", "Gravitera",
                    "Harmonia", "Havera", "Honbra", "Ignitera", "Inabra", "Issobra", "Jacabra",
                    "Jadebra", "Jaguna", "Kalanta", "Kalibra", "Kalimera", "Karmeya", "Lobera",
                    "Luminera", "Lutabra", "Luzibra", "Malivora", "Menera", "Monstera", "Monza",
                    "Mystera", "Nefera", "Nevia", "Noctalia", "Obscubra", "Oceanis", "Ombra",
                    "Ourobra", "Pacera", "Peloria", "Penumbra", "Premia", "Quebra", "Quelibra",
                    "Quidera", "Quintera", "Rasteibra", "Refugia", "Retalia", "Runera", "Secura",
                    "Serdebra", "Solidera", "Sombra", "Sonira", "Stralis", "Talera", "Temera",
                    "Tempestera", "Terribra", "Thyria", "Tornabra", "Ulera", "Unebra", "Ustebra",
                    "Vandera", "Venebra", "Victoris", "Vitera", "Vunira", "Wadira", "Wildera",
                    "Wintera", "Xyla", "Xybra", "Xymera", "Yara", "Yonabra", "Yovera", "Yubra", "Zephyra");

            tibiaCoinService.sincronizarTodosOsMundos(mundosManuais);

            attributes.addFlashAttribute("mensagem",
                    "Sincronização iniciada para " + mundosManuais.size() + " mundos.");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao sincronizar: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
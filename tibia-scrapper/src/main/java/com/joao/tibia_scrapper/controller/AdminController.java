package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.Enums.CategoriaEnum;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import com.joao.tibia_scrapper.repository.TopicoRepository;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import com.joao.tibia_scrapper.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private ScraperService scraperService;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalEquipamentos", equipamentoRepository.count());
        model.addAttribute("totalTopicos", topicoRepository.count());
        
        model.addAttribute("totalPersonagens", usuarioRepository.countPersonagensLinkados());

        model.addAttribute("statsVocacoes", agruparDados(usuarioRepository.countByVocation()));
        model.addAttribute("statsMundos", agruparDados(usuarioRepository.countByWorld()));
        model.addAttribute("statsCidades", agruparDados(usuarioRepository.countByCity()));

        return "admin"; 
    }

    private Map<String, Long> agruparDados(List<Object[]> resultados) {
        Map<String, Long> map = new HashMap<>();
        for (Object[] row : resultados) {
            String chave = row[0] != null && !row[0].toString().trim().isEmpty() ? row[0].toString() : "Desconhecido";
            Long valor = (Long) row[1];
            map.put(chave, valor);
        }
        return map;
    }

    @PostMapping("/importar-tudo")
    public String importarTudo(RedirectAttributes redirectAttributes) {
        try {
            for (CategoriaEnum cat : CategoriaEnum.values()) {
                scraperService.importarCategoria(cat.getNome(), cat.getSubUrl());
                Thread.sleep(1000); 
            }
            redirectAttributes.addFlashAttribute("sucesso", "Base de dados atualizada com sucesso! Todas as categorias foram importadas.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            redirectAttributes.addFlashAttribute("erro", "Erro: O processo de importação foi interrompido.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao executar o Scraper: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/atualizar-itens-api")
    public ResponseEntity<String> iniciarAtualizacaoApi() {
        new Thread(() -> scraperService.atualizarEquipamentosViaApi()).start();
        return ResponseEntity.ok("Processo de atualização via API iniciado. Acompanhe os logs na consola do Spring Boot!");
    }
}
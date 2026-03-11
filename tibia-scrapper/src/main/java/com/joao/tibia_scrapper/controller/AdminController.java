package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.Enums.CategoriaEnum;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.*;
import com.joao.tibia_scrapper.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final TopicoRepository topicoRepository;
    private final HuntRecordRepository huntRecordRepository;
    private final ScraperService scraperService;

    @GetMapping
    public String adminDashboard(@RequestParam(required = false, defaultValue = "") String busca, Model model) {
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalEquipamentos", equipamentoRepository.count());
        model.addAttribute("totalTopicos", topicoRepository.count());
        model.addAttribute("totalPersonagens", usuarioRepository.countPersonagensLinkados());
        model.addAttribute("totalHunts", huntRecordRepository.count());

        model.addAttribute("statsVocacoes", agruparDados(usuarioRepository.countByVocation()));
        model.addAttribute("statsMundos", agruparDados(usuarioRepository.countByWorld()));
        model.addAttribute("statsCidades", agruparDados(usuarioRepository.countByCity()));

        model.addAttribute("topAutores", agruparDados(topicoRepository.findTopAutores(PageRequest.of(0, 5))));
        model.addAttribute("topHunts", agruparDados(huntRecordRepository.findTopHuntCreators(PageRequest.of(0, 5))));

        model.addAttribute("topLevels", usuarioRepository.findTop5ByCharNameIsNotNullOrderByCharLevelDesc());
        model.addAttribute("topMagic", usuarioRepository.findTop5ByCharNameIsNotNullOrderByMagicLevelDesc());
        model.addAttribute("topDistance", usuarioRepository.findTop5ByCharNameIsNotNullOrderByDistanceSkillDesc());
        model.addAttribute("topSword", usuarioRepository.findTop5ByCharNameIsNotNullOrderBySwordSkillDesc());
        model.addAttribute("topAxe", usuarioRepository.findTop5ByCharNameIsNotNullOrderByAxeSkillDesc());
        model.addAttribute("topClub", usuarioRepository.findTop5ByCharNameIsNotNullOrderByClubSkillDesc());
        model.addAttribute("topFist", usuarioRepository.findTop5ByCharNameIsNotNullOrderByFistSkillDesc());
        model.addAttribute("topShielding", usuarioRepository.findTop5ByCharNameIsNotNullOrderByShieldingSkillDesc());
        model.addAttribute("topFishing", usuarioRepository.findTop5ByCharNameIsNotNullOrderByFishingSkillDesc());

        model.addAttribute("ultimosUsuarios", usuarioRepository.findUsuariosGerenciamento(busca));
        model.addAttribute("busca", busca);

        List<String> logs = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");
        logs.add("[" + LocalDateTime.now().format(df) + "] Admin acessou o painel de controle.");
        model.addAttribute("systemLogs", logs);

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
            redirectAttributes.addFlashAttribute("sucesso", "Base de dados atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao executar o Scraper: " + e.getMessage());
        }
        return "redirect:/admin?tab=sistema";
    }

    @PostMapping("/atualizar-itens-api")
    public ResponseEntity<String> iniciarAtualizacaoApi() {
        new Thread(scraperService::atualizarEquipamentosViaApi).start();
        return ResponseEntity.ok("Processo de atualização via API iniciado na consola!");
    }

    @PostMapping("/usuario/promover/{id}")
    public String promoverUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioRepository.findById(id).ifPresent(u -> {
            if (!"ADMIN".equals(u.getRole())) {
                u.setRole("MODERATOR");
                usuarioRepository.save(u);
                redirectAttributes.addFlashAttribute("sucesso", u.getUsername() + " promovido a Moderador.");
            }
        });
        return "redirect:/admin?tab=contas";
    }

    @PostMapping("/usuario/rebaixar/{id}")
    public String rebaixarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioRepository.findById(id).ifPresent(u -> {
            if (!"ADMIN".equals(u.getRole())) {
                u.setRole("USER");
                usuarioRepository.save(u);
                redirectAttributes.addFlashAttribute("sucesso", u.getUsername() + " rebaixado para Usuário.");
            }
        });
        return "redirect:/admin?tab=contas";
    }

    @PostMapping("/usuario/banir/{id}")
    public String banirUsuario(@PathVariable Long id, @RequestParam(defaultValue = "0") Integer horasBan,
            RedirectAttributes redirectAttributes) {
        usuarioRepository.findById(id).ifPresent(u -> {
            if ("BANNED".equals(u.getRole())) {
                u.setRole("USER");
                u.setBanExpiration(null);
                redirectAttributes.addFlashAttribute("sucesso", u.getUsername() + " teve o acesso restaurado.");
            } else if (!"ADMIN".equals(u.getRole())) {
                u.setRole("BANNED");
                u.setBanExpiration(horasBan > 0 ? LocalDateTime.now().plusHours(horasBan) : null);
                redirectAttributes.addFlashAttribute("sucesso", u.getUsername()
                        + (horasBan > 0 ? " banido por " + horasBan + " horas." : " banido permanentemente."));
            }
            usuarioRepository.save(u);
        });
        return "redirect:/admin?tab=contas";
    }

    @PostMapping("/usuario/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioRepository.findById(id).ifPresent(u -> {
            if (!"ADMIN".equals(u.getRole())) {
                try {
                    usuarioRepository.delete(u);
                    redirectAttributes.addFlashAttribute("sucesso", "Conta eliminada com sucesso.");
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("erro",
                            "Erro: A conta possui registos dependentes (Hunts, Tópicos).");
                }
            }
        });
        return "redirect:/admin?tab=contas";
    }

    @PostMapping("/importar-runas")
    @ResponseBody
    public ResponseEntity<String> importarRunasWiki() {
        new Thread(scraperService::importarRunasTibiaWikiBrCompleto).start();
        return ResponseEntity.ok("Importação de Runas iniciada.");
    }

    @PostMapping("/importar-magias")
    @ResponseBody
    public ResponseEntity<String> importarMagiasApi() {
        new Thread(scraperService::importarMagiasTibiaWikiBr).start();
        return ResponseEntity.ok("Importação de Magias iniciada.");
    }

    @PostMapping("/migrar-criaturas-local")
    @ResponseBody
    public ResponseEntity<String> migrarCriaturas() {
        new Thread(scraperService::migrarCriaturasLocal).start();
        return ResponseEntity.ok("Migração local iniciada!");
    }

    @PostMapping("/importar-criaturas-json")
    @ResponseBody
    public ResponseEntity<String> importarCriaturasJson(
            @RequestParam(defaultValue = "C:/tibia-enciclopedia/creatures") String pasta) {
        new Thread(() -> scraperService.importarCriaturasJson(pasta)).start();
        return ResponseEntity.ok("Importação de Criaturas iniciada.");
    }
}
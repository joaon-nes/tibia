package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.dto.NotificacaoDTO;
import com.joao.tibia_scrapper.model.Notificacao;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.NotificacaoRepository;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarNotificacoes(Model model, Principal principal) {
        if (principal == null)
            return "redirect:/login";
        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
        model.addAttribute("notificacoes", notificacaoRepository.findByDestinatarioOrderByDataDesc(usuario));
        return "notificacoes";
    }

    @GetMapping("/api/resumo")
    @ResponseBody
    public List<NotificacaoDTO> getResumo(Principal principal) {
        if (principal == null)
            return List.of();
        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
        List<Notificacao> ultimas = notificacaoRepository.findTop5ByDestinatarioOrderByDataDesc(usuario);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");

        return ultimas.stream().map(n -> new NotificacaoDTO(
                n.getId(), n.getMensagem(), n.getRemetente().getUsername(),
                "/notificacoes/ler/" + n.getId(), n.getIcone(), n.isLida(),
                n.getData().format(formatter))).collect(Collectors.toList());
    }

    @GetMapping("/api/count")
    @ResponseBody
    public Long getCount(Principal principal) {
        if (principal == null)
            return 0L;
        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
        return notificacaoRepository.countByDestinatarioAndLidaFalse(usuario);
    }

    @GetMapping("/ler/{id}")
    public String lerNotificacao(@PathVariable Long id, Principal principal) {
        Notificacao notif = notificacaoRepository.findById(id).orElse(null);
        if (notif != null && principal != null && notif.getDestinatario().getUsername().equals(principal.getName())) {
            notif.setLida(true);
            notificacaoRepository.save(notif);
            return "redirect:" + notif.getLink();
        }
        return "redirect:/notificacoes";
    }

    @GetMapping("/ler-todas")
    public String lerTodas(Principal principal) {
        if (principal != null) {
            Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
            List<Notificacao> notificacoes = notificacaoRepository.findByDestinatarioOrderByDataDesc(usuario);
            for (Notificacao n : notificacoes) {
                if (!n.isLida()) {
                    n.setLida(true);
                    notificacaoRepository.save(n);
                }
            }
        }
        return "redirect:/notificacoes";
    }
}
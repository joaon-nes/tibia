package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.model.AnuncioParty;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.AnuncioPartyRepository;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import com.joao.tibia_scrapper.model.Notificacao;
import com.joao.tibia_scrapper.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/buscar-party")
public class PartyController {

    @Autowired
    private AnuncioPartyRepository partyRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @GetMapping
    public String listarAnuncios(Model model, Principal principal) {
        List<AnuncioParty> anuncios = partyRepository.findAllByOrderByDataCriacaoDesc();
        model.addAttribute("anuncios", anuncios);

        if (principal != null) {
            Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
            model.addAttribute("usuarioLogado", usuario);
            model.addAttribute("jaAnunciou", partyRepository.existsByUsuario(usuario));
        }
        return "buscar-party";
    }

    @PostMapping("/anunciar")
    public String criarAnuncio(@RequestParam String tipoBusca,
            @RequestParam String objetivo,
            @RequestParam(required = false) String objetivoEspecifico,
            Principal principal,
            RedirectAttributes ra) {
        if (principal == null)
            return "redirect:/login";

        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);

        if (usuario == null || usuario.getCharName() == null) {
            ra.addFlashAttribute("erro", "Vincule um personagem antes de procurar party.");
            return "redirect:/personagem";
        }

        String objetivoFinal = objetivo;
        if (objetivoEspecifico != null && !objetivoEspecifico.trim().isEmpty()) {
            objetivoFinal = objetivo + " (" + objetivoEspecifico.trim() + ")";
        }

        partyRepository.deleteByUsuario(usuario);

        AnuncioParty anuncio = new AnuncioParty();
        anuncio.setUsuario(usuario);
        anuncio.setTipoBusca(tipoBusca);
        anuncio.setObjetivo(objetivoFinal);

        partyRepository.save(anuncio);

        ra.addFlashAttribute("sucesso", "Anúncio publicado! Boa sorte na busca.");
        return "redirect:/buscar-party";
    }

    @PostMapping("/interesse/{id}")
    public String demonstrarInteresse(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        if (principal == null)
            return "redirect:/login";

        Usuario interessado = usuarioRepository.findByUsername(principal.getName()).orElse(null);
        AnuncioParty anuncio = partyRepository.findById(id).orElse(null);

        if (interessado == null || anuncio == null) {
            ra.addFlashAttribute("erro", "Não foi possível processar a solicitação.");
            return "redirect:/buscar-party";
        }

        if (interessado.getId().equals(anuncio.getUsuario().getId())) {
            ra.addFlashAttribute("erro", "Você não pode demonstrar interesse no seu próprio anúncio.");
            return "redirect:/buscar-party";
        }

        int levelAnunciante = anuncio.getUsuario().getCharLevel() != null ? anuncio.getUsuario().getCharLevel() : 0;
        int levelInteressado = interessado.getCharLevel() != null ? interessado.getCharLevel() : 0;

        int minLevel = (int) Math.ceil(levelAnunciante * 2.0 / 3.0);
        int maxLevel = (int) Math.floor(levelAnunciante * 1.5);

        if (levelInteressado < minLevel || levelInteressado > maxLevel) {
            ra.addFlashAttribute("erro", "O seu Level (" + levelInteressado + ") está fora da faixa de Shared XP ("
                    + minLevel + " - " + maxLevel + ") permitida para este jogador.");
            return "redirect:/buscar-party";
        }

        Notificacao notif = new Notificacao();
        notif.setDestinatario(anuncio.getUsuario());
        notif.setRemetente(interessado);

        String nomeChar = interessado.getCharName() != null ? interessado.getCharName() : interessado.getUsername();
        notif.setMensagem("O jogador " + nomeChar + " (Level " + interessado.getCharLevel()
                + ") tem interesse no seu anúncio para " + anuncio.getObjetivo() + "!");
        notif.setLink("/buscar-party");
        notif.setLida(false);
        notif.setData(java.time.LocalDateTime.now());

        notificacaoRepository.save(notif);

        ra.addFlashAttribute("sucesso",
                "Notificação enviada com sucesso para " + anuncio.getUsuario().getCharName() + "!");
        return "redirect:/buscar-party";
    }

    @PostMapping("/remover")
    public String removerAnuncio(Principal principal, RedirectAttributes ra) {
        if (principal == null)
            return "redirect:/login";

        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);

        if (usuario != null) {
            partyRepository.deleteByUsuario(usuario);
            ra.addFlashAttribute("sucesso", "O seu anúncio foi removido com sucesso!");
        }

        return "redirect:/buscar-party";
    }
}
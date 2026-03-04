package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.dto.CharacterDTO;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import com.joao.tibia_scrapper.service.CharacterScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/personagem")
public class PersonagemViewController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CharacterScraperService characterService;

    @GetMapping
    public String paginaPersonagem(Model model, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(principal.getName());
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuario", usuario);

            boolean temPersonagem = usuario.getCharName() != null && !usuario.getCharName().isEmpty();
            model.addAttribute("temPersonagem", temPersonagem);
        }

        return "personagem";
    }

    @PostMapping("/vincular")
    public String vincularPersonagem(@RequestParam String nomePersonagem, Principal principal,
            RedirectAttributes redirectAttributes) {
        if (principal == null)
            return "redirect:/login";

        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
        if (usuario == null)
            return "redirect:/login";

        CharacterDTO character = characterService.buscarPersonagem(nomePersonagem);

        if (character == null) {
            redirectAttributes.addFlashAttribute("erro",
                    "Personagem não encontrado. Verifique se o nome está correto.");
            return "redirect:/personagem";
        }

        usuario.setCharName(character.name());
        usuario.setCharLevel(character.level());
        usuario.setCharVocation(character.vocation());
        usuario.setCharWorld(character.world());
        usuario.setCharResidence(character.residence());

        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("sucesso", "Personagem " + character.name() + " vinculado com sucesso!");
        return "redirect:/personagem";
    }

    @PostMapping("/desvincular")
    public String desvincularPersonagem(Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
        if (usuario != null) {
            usuario.setCharName(null);
            usuario.setCharLevel(null);
            usuario.setCharVocation(null);
            usuario.setCharWorld(null);
            usuario.setCharResidence(null);
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Personagem desvinculado com sucesso.");
        }
        return "redirect:/personagem";
    }

    @PostMapping("/atualizar-skills")
    public String atualizarSkills(
            @RequestParam(defaultValue = "0") Integer magicLevel,
            @RequestParam(defaultValue = "10") Integer fistSkill,
            @RequestParam(defaultValue = "10") Integer clubSkill,
            @RequestParam(defaultValue = "10") Integer swordSkill,
            @RequestParam(defaultValue = "10") Integer axeSkill,
            @RequestParam(defaultValue = "10") Integer distanceSkill,
            @RequestParam(defaultValue = "10") Integer shieldingSkill,
            @RequestParam(defaultValue = "10") Integer fishingSkill,
            Principal principal, RedirectAttributes redirectAttributes) {

        if (principal == null)
            return "redirect:/login";

        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
        if (usuario != null) {
            usuario.setMagicLevel(magicLevel);
            usuario.setFistSkill(fistSkill);
            usuario.setClubSkill(clubSkill);
            usuario.setSwordSkill(swordSkill);
            usuario.setAxeSkill(axeSkill);
            usuario.setDistanceSkill(distanceSkill);
            usuario.setShieldingSkill(shieldingSkill);
            usuario.setFishingSkill(fishingSkill);

            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Skills atualizadas com sucesso!");
        }
        return "redirect:/personagem";
    }
}
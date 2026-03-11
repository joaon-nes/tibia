package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.dto.CharacterDTO;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import com.joao.tibia_scrapper.service.CharacterScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/personagem")
@RequiredArgsConstructor
public class PersonagemViewController {

    private final UsuarioRepository usuarioRepository;
    private final CharacterScraperService characterService;

    @GetMapping
    public String paginaPersonagem(Model model, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        usuarioRepository.findByUsername(principal.getName()).ifPresent(usuario -> {
            model.addAttribute("usuario", usuario);
            model.addAttribute("temPersonagem", usuario.getCharName() != null && !usuario.getCharName().isEmpty());
        });

        return "personagem";
    }

    @PostMapping("/vincular")
    public String vincularPersonagem(@RequestParam String nomePersonagem, Principal principal, RedirectAttributes ra) {
        if (principal == null)
            return "redirect:/login";

        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
        if (usuario == null)
            return "redirect:/login";

        CharacterDTO character = characterService.buscarPersonagem(nomePersonagem);

        if (character == null) {
            ra.addFlashAttribute("erro", "Personagem não encontrado. Verifique se o nome está correto.");
            return "redirect:/personagem";
        }

        usuario.setCharName(character.name());
        usuario.setCharLevel(character.level());
        usuario.setCharVocation(character.vocation());
        usuario.setCharWorld(character.world());
        usuario.setCharResidence(character.residence());

        usuarioRepository.save(usuario);
        ra.addFlashAttribute("sucesso", "Personagem vinculado com sucesso!");
        return "redirect:/personagem";
    }

    @PostMapping("/desvincular")
    public String desvincularPersonagem(Principal principal, RedirectAttributes ra) {
        if (principal != null) {
            usuarioRepository.findByUsername(principal.getName()).ifPresent(usuario -> {
                usuario.setCharName(null);
                usuario.setCharLevel(null);
                usuario.setCharVocation(null);
                usuario.setCharWorld(null);
                usuario.setCharResidence(null);
                usuario.setSetsEquipamentos(null);
                usuarioRepository.save(usuario);
                ra.addFlashAttribute("sucesso", "Personagem desvinculado com sucesso.");
            });
        }
        return "redirect:/personagem";
    }

    @PostMapping("/atualizar-skills")
    public String atualizarSkills(
            @RequestParam(defaultValue = "0") Integer magicLevel, @RequestParam(defaultValue = "10") Integer fistSkill,
            @RequestParam(defaultValue = "10") Integer clubSkill, @RequestParam(defaultValue = "10") Integer swordSkill,
            @RequestParam(defaultValue = "10") Integer axeSkill,
            @RequestParam(defaultValue = "10") Integer distanceSkill,
            @RequestParam(defaultValue = "10") Integer shieldingSkill,
            @RequestParam(defaultValue = "10") Integer fishingSkill,
            Principal principal, RedirectAttributes ra) {

        if (principal != null) {
            usuarioRepository.findByUsername(principal.getName()).ifPresent(usuario -> {
                usuario.setMagicLevel(magicLevel);
                usuario.setFistSkill(fistSkill);
                usuario.setClubSkill(clubSkill);
                usuario.setSwordSkill(swordSkill);
                usuario.setAxeSkill(axeSkill);
                usuario.setDistanceSkill(distanceSkill);
                usuario.setShieldingSkill(shieldingSkill);
                usuario.setFishingSkill(fishingSkill);
                usuarioRepository.save(usuario);
                ra.addFlashAttribute("sucesso", "Skills atualizadas com sucesso!");
            });
        }
        return "redirect:/personagem";
    }

    @PostMapping("/atualizar-dados")
    public String atualizarDadosPersonagem(Principal principal, RedirectAttributes ra) {
        if (principal == null)
            return "redirect:/login";

        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
        if (usuario == null || usuario.getCharName() == null)
            return "redirect:/personagem";

        CharacterDTO character = characterService.buscarPersonagem(usuario.getCharName());

        if (character == null) {
            ra.addFlashAttribute("erro", "Erro ao tentar atualizar os dados no Tibia.");
            return "redirect:/personagem";
        }

        usuario.setCharLevel(character.level());
        usuario.setCharVocation(character.vocation());
        usuario.setCharWorld(character.world());
        usuario.setCharResidence(character.residence());
        usuarioRepository.save(usuario);

        ra.addFlashAttribute("sucesso", "Dados do personagem atualizados com sucesso!");
        return "redirect:/personagem";
    }
}
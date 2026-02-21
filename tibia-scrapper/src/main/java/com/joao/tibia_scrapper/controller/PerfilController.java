package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import com.joao.tibia_scrapper.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Base64;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private UsuarioService service;

    @GetMapping
    public String abrirPerfil(Model model, Principal principal) {
        String username = principal.getName();
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido na sessão"));
        model.addAttribute("usuario", usuario);
        return "editar-perfil";
    }

    @PostMapping("/atualizar")
    public String atualizarPerfil(@RequestParam String nome, @RequestParam(required = false) String senha,
            @RequestParam(required = false) String charName, @RequestParam(required = false) Integer charLevel,
            @RequestParam(required = false) String charVocation, @RequestParam(required = false) String charWorld,
            @RequestParam(required = false) String charResidence,
            @RequestParam(required = false) MultipartFile imagemPerfil,
            Principal principal) {
        try {
            String avatarBase64 = null;
            if (imagemPerfil != null && !imagemPerfil.isEmpty()) {
                byte[] bytes = imagemPerfil.getBytes();
                String tipo = imagemPerfil.getContentType();
                avatarBase64 = "data:" + tipo + ";base64," + Base64.getEncoder().encodeToString(bytes);
            }
            service.atualizarUsuarioCompleto(principal.getName(), nome, senha, charName, charLevel, charVocation,
                    charWorld, charResidence, avatarBase64);
            return "redirect:/perfil?sucesso";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/perfil?erro";
        }
    }
}
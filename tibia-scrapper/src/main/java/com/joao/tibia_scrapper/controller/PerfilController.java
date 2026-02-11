package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import com.joao.tibia_scrapper.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

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
    public String atualizarPerfil(@RequestParam String nome,
                                  @RequestParam(required = false) String senha,
                                  Principal principal,
                                  Model model) {
        try {
            service.atualizarUsuario(principal.getName(), nome, senha);
            return "redirect:/perfil?sucesso";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/perfil?erro";
        }
    }
}
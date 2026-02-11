package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(@RequestParam String username,
                                    @RequestParam String password,
                                    @RequestParam String nome,
                                    @RequestParam String email,
                                    Model model) {
        try {
            usuarioService.cadastrarUsuario(username, password, nome, email);
            return "redirect:/login?cadastrado=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            
            model.addAttribute("username", username);
            model.addAttribute("nome", nome);
            model.addAttribute("email", email);
            
            return "cadastro";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro inesperado ao cadastrar. Tente novamente.");
            return "cadastro";
        }
    }
}
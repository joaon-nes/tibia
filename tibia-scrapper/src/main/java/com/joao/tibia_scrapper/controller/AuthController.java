package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
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
    public String login(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");

        if (referrer != null && !referrer.contains("/login") && !referrer.contains("/cadastro")) {
            request.getSession().setAttribute("url_prior_login", referrer);
        }

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
            e.printStackTrace();

            String mensagemErro = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();

            model.addAttribute("erro", "Erro técnico: " + mensagemErro);

            model.addAttribute("username", username);
            model.addAttribute("nome", nome);
            model.addAttribute("email", email);

            return "cadastro";
        }
    }
}
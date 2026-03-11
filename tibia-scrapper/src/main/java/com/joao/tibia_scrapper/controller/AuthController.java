package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");

        if (referrer != null &&
                !referrer.contains("/login") &&
                !referrer.contains("/cadastro") &&
                !referrer.contains("/recuperar-senha") &&
                !referrer.contains("/resetar-senha") &&
                !referrer.contains("/confirmar-email")) {

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
            RedirectAttributes ra,
            Model model) {
        try {
            usuarioService.cadastrarUsuario(username, password, nome, email);
            ra.addFlashAttribute("sucesso",
                    "Conta criada com sucesso! Verifique a sua caixa de entrada para confirmar o email.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("nome", nome);
            model.addAttribute("email", email);
            return "cadastro";
        } catch (Exception e) {
            String mensagemErro = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            model.addAttribute("erro", "Erro técnico: " + mensagemErro);
            model.addAttribute("username", username);
            model.addAttribute("nome", nome);
            model.addAttribute("email", email);
            return "cadastro";
        }
    }

    @GetMapping("/confirmar-email")
    public String confirmarEmail(@RequestParam("token") String token, RedirectAttributes ra) {
        boolean confirmado = usuarioService.confirmarEmail(token);

        if (confirmado) {
            ra.addFlashAttribute("sucesso", "Email confirmado com sucesso! Já pode iniciar sessão.");
        } else {
            ra.addFlashAttribute("erro", "Link de confirmação inválido ou já utilizado.");
        }

        return "redirect:/login";
    }

    @GetMapping("/recuperar-senha")
    public String recuperarSenha() {
        return "recuperar-senha";
    }

    @PostMapping("/recuperar-senha")
    public String processarRecuperacaoSenha(@RequestParam String email, RedirectAttributes ra) {
        usuarioService.solicitarRecuperacaoSenha(email);
        ra.addFlashAttribute("sucesso",
                "Se o email estiver registado, enviaremos um link de recuperação. Verifique também o seu Spam!");
        return "redirect:/login";
    }

    @GetMapping("/resetar-senha")
    public String resetarSenha(@RequestParam("token") String token, Model model, RedirectAttributes ra) {
        try {
            usuarioService.validarTokenRecuperacao(token);
            model.addAttribute("token", token);
            return "resetar-senha";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/login";
        }
    }

    @PostMapping("/resetar-senha")
    public String processarResetSenha(@RequestParam("token") String token,
            @RequestParam("novaSenha") String novaSenha,
            RedirectAttributes ra) {
        try {
            Usuario usuario = usuarioService.validarTokenRecuperacao(token);
            usuarioService.atualizarSenhaEsquecida(usuario, novaSenha);
            ra.addFlashAttribute("sucesso", "Senha alterada com sucesso! Já pode iniciar sessão.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/login";
        }
    }
}
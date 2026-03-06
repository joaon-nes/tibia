package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import com.joao.tibia_scrapper.service.TotpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mfa")
public class MfaController {

    @Autowired
    private TotpService totpService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/setup")
    public String setup2FA(Authentication authentication, Model model) {
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName()).orElseThrow();

        if (Boolean.TRUE.equals(usuario.getIsUsing2FA())) {
            model.addAttribute("mensagem", "A Autenticação de 2 Fatores já está ativada.");
            return "setup-2fa";
        }

        String secret = usuario.getSecret2FA();
        if (secret == null || secret.trim().isEmpty()) {
            secret = totpService.generateSecret();
            usuario.setSecret2FA(secret);
            usuarioRepository.save(usuario);
        }

        try {
            String label = (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) ? usuario.getEmail()
                    : usuario.getUsername();
            String qrCodeImage = totpService.getQrCodeImageUri(secret, label);

            model.addAttribute("qrCodeImage", qrCodeImage);
            model.addAttribute("secret", secret);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao gerar o QR Code.");
        }

        return "setup-2fa";
    }

    @PostMapping("/verify")
    public String verify2FA(@RequestParam("code") String code, Authentication authentication, Model model) {
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName()).orElseThrow();

        if (totpService.verifyCode(usuario.getSecret2FA(), code)) {
            usuario.setIsUsing2FA(true);
            usuarioRepository.save(usuario);
            return "redirect:/perfil?sucesso=true";
        } else {
            model.addAttribute("erro", "Código inválido. Tente novamente.");
            try {
                String qrCodeImage = totpService.getQrCodeImageUri(usuario.getSecret2FA(), usuario.getEmail());
                model.addAttribute("qrCodeImage", qrCodeImage);
                model.addAttribute("secret", usuario.getSecret2FA());
            } catch (Exception e) {
            }
            return "setup-2fa";
        }
    }

    @GetMapping("/disable")
    public String disable2FA(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName()).orElseThrow();

        usuario.setIsUsing2FA(false);
        usuario.setSecret2FA(null);
        usuarioRepository.save(usuario);

        return "redirect:/perfil?sucesso=true";
    }
}
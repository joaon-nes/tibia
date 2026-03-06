package com.joao.tibia_scrapper.config;

import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import com.joao.tibia_scrapper.service.TotpService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private final TotpService totpService;
    private final UsuarioRepository usuarioRepository;

    public CustomAuthenticationProvider(TotpService totpService, UsuarioRepository usuarioRepository) {
        this.totpService = totpService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        System.out.println(">>> [LOGIN] 1. Tentando login para: " + auth.getName());

        Authentication result;
        try {
            result = super.authenticate(auth);
            System.out.println(">>> [LOGIN] 2. Senha correta!");
        } catch (AuthenticationException e) {
            System.out.println(">>> [LOGIN] 2. FALHA: Senha incorreta ou utilizador não existe.");
            throw e;
        }

        Usuario usuario = usuarioRepository.findByUsername(result.getName()).orElse(null);

        if (usuario != null && Boolean.TRUE.equals(usuario.getIsUsing2FA())) {
            System.out.println(">>> [LOGIN] 3. Utilizador tem 2FA ativo. A verificar código...");

            String verificationCode = null;
            if (auth.getDetails() instanceof CustomWebAuthenticationDetails details) {
                verificationCode = details.getVerificationCode();
            }

            if (verificationCode == null || verificationCode.trim().isEmpty()) {
                System.out.println(">>> [LOGIN] 4. FALHA: Código 2FA em branco.");
                throw new BadCredentialsException("Código 2FA ausente.");
            }

            if (!totpService.verifyCode(usuario.getSecret2FA(), verificationCode)) {
                System.out.println(">>> [LOGIN] 4. FALHA: Código 2FA inválido (" + verificationCode + ").");
                throw new BadCredentialsException("Código 2FA inválido.");
            } else {
                System.out.println(">>> [LOGIN] 4. SUCESSO: Código 2FA válido!");
            }
        } else {
            System.out.println(">>> [LOGIN] 3. Utilizador NÃO tem 2FA. Login concluído.");
        }

        return result;
    }
}
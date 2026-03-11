package com.joao.tibia_scrapper.service;

import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if ("BANNED".equals(usuario.getRole()) && usuario.getBanExpiration() != null) {
            if (LocalDateTime.now().isAfter(usuario.getBanExpiration())) {
                usuario.setRole("USER");
                usuario.setBanExpiration(null);
                repository.save(usuario);
            }
        }
        /* bloqueio de login para email não confirmado
        if (!usuario.getEmailConfirmado() && !"ADMIN".equals(usuario.getRole())) {
             throw new RuntimeException("Por favor, confirme o seu email antes de fazer login.");
        }
        */

        return usuario;
    }

    public void cadastrarUsuario(String username, String password, String nome, String email) {
        if (repository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("O nome de utilizador já está em uso.");
        }
        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email já está registado.");
        }

        String senhaCriptografada = passwordEncoder.encode(password);
        Usuario novoUsuario = new Usuario(username, senhaCriptografada, "USER", nome, email);
        
        String token = UUID.randomUUID().toString();
        novoUsuario.setVerificationToken(token);
        
        repository.save(novoUsuario);

        new Thread(() -> emailService.enviarEmailConfirmacao(email, token)).start();
    }

    public boolean confirmarEmail(String token) {
        Optional<Usuario> usuarioOpt = repository.findByVerificationToken(token);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEmailConfirmado(true);
            usuario.setVerificationToken(null);
            repository.save(usuario);
            return true;
        }
        return false;
    }

    public void atualizarUsuarioCompleto(String username, String novoNome, String novaSenha,
            String charName, Integer charLevel, String charVocation,
            String charWorld, String charResidence, String avatarBase64) {

        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));

        usuario.setNome(novoNome);

        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(novaSenha));
        }
        if (avatarBase64 != null) {
            usuario.setAvatar(avatarBase64);
        }
        if (charName != null && !charName.isEmpty()) {
            usuario.setCharName(charName);
            usuario.setCharLevel(charLevel);
            usuario.setCharVocation(charVocation);
            usuario.setCharWorld(charWorld);
            usuario.setCharResidence(charResidence);
        }
        repository.save(usuario);
    }

    public void solicitarRecuperacaoSenha(String email) {
        Optional<Usuario> usuarioOpt = repository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = UUID.randomUUID().toString();
            usuario.setResetPasswordToken(token);
            usuario.setResetPasswordTokenExpiration(LocalDateTime.now().plusHours(1));
            repository.save(usuario);
            
            new Thread(() -> emailService.enviarEmailRecuperacaoSenha(email, token)).start();
        }
    }

    public Usuario validarTokenRecuperacao(String token) {
        Usuario usuario = repository.findByResetPasswordToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido ou não encontrado."));
                
        if (usuario.getResetPasswordTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Este link de recuperação expirou. Por favor, solicite um novo.");
        }
        return usuario;
    }

    public void atualizarSenhaEsquecida(Usuario usuario, String novaSenha) {
        usuario.setPassword(passwordEncoder.encode(novaSenha));
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordTokenExpiration(null);
        repository.save(usuario);
    }
}
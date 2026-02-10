package com.joao.tibia_scrapper.service;

import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRole())
                .build();
    }

    public void cadastrarUsuario(String username, String password, String nome, String email) {
        if (repository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("O nome de usuário já está em uso.");
        }
        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email já está cadastrado.");
        }
        String senhaCriptografada = passwordEncoder.encode(password);
        Usuario novoUsuario = new Usuario(username, senhaCriptografada, "USER", nome, email);
        repository.save(novoUsuario);
    }

    public void salvarUsuario(String username, String password) {
        String senhaCriptografada = passwordEncoder.encode(password);
        Usuario novoUsuario = new Usuario(username, senhaCriptografada, "USER", username, username);
        repository.save(novoUsuario);
    }

    public void atualizarUsuario(String username, String novoNome, String novaSenha) {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));
        usuario.setNome(novoNome);
        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(novaSenha));
        }
        repository.save(usuario);
    }
}
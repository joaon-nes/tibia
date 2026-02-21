package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.model.Notificacao;
import com.joao.tibia_scrapper.model.Postagem;
import com.joao.tibia_scrapper.model.Topico;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.NotificacaoRepository;
import com.joao.tibia_scrapper.repository.PostagemRepository;
import com.joao.tibia_scrapper.repository.TopicoRepository;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/comunidade")
public class ComunidadeController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private PostagemRepository postagemRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @GetMapping
    public String listarTopicos(Model model) {
        model.addAttribute("topicos", topicoRepository.findAllByOrderByDataCriacaoDesc());
        return "comunidade";
    }

    @GetMapping("/criar")
    public String formCriarTopico() {
        return "criar-topico";
    }

    @PostMapping("/criar")
    public String salvarTopico(@RequestParam String titulo, @RequestParam String conteudo, Principal principal) {
        if (principal == null)
            return "redirect:/login";
        Usuario autor = usuarioRepository.findByUsername(principal.getName()).orElse(null);
        if (autor == null)
            return "redirect:/login";

        Topico topico = new Topico();
        topico.setTitulo(titulo);
        topico.setConteudo(conteudo);
        topico.setAutor(autor);
        topico.setDataCriacao(LocalDateTime.now());
        topicoRepository.save(topico);

        return "redirect:/comunidade";
    }

    @GetMapping("/topico/{id}")
    public String verTopico(@PathVariable Long id, Model model, Principal principal) {
        Topico topico = topicoRepository.findById(id).orElse(null);
        if (topico == null)
            return "redirect:/comunidade";

        if (principal != null) {
            Usuario logado = usuarioRepository.findByUsername(principal.getName()).orElse(null);
            model.addAttribute("usuarioLogado", logado);
        }
        model.addAttribute("topico", topico);
        return "topico";
    }

    @PostMapping("/topico/{id}/responder")
    public String responderTopico(@PathVariable Long id, @RequestParam String conteudo, Principal principal) {
        if (principal == null)
            return "redirect:/login";
        Topico topico = topicoRepository.findById(id).orElse(null);
        Usuario autorResposta = usuarioRepository.findByUsername(principal.getName()).orElse(null);

        if (topico != null && autorResposta != null && !conteudo.trim().isEmpty()) {
            Postagem post = new Postagem();
            post.setConteudo(conteudo);
            post.setAutor(autorResposta);
            post.setTopico(topico);
            post.setDataPostagem(LocalDateTime.now());
            postagemRepository.save(post);

            if (!topico.getAutor().equals(autorResposta)) {
                Notificacao notif = new Notificacao(topico.getAutor(), autorResposta,
                        "respondeu no seu tópico: " + topico.getTitulo(), "/comunidade/topico/" + topico.getId());
                notificacaoRepository.save(notif);
            }
        }
        return "redirect:/comunidade/topico/" + id;
    }

    @PostMapping("/topico/{id}/votar")
    public String votarTopico(@PathVariable Long id, Principal principal) {
        if (principal == null)
            return "redirect:/login";
        Topico topico = topicoRepository.findById(id).orElse(null);
        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);

        if (topico != null && usuario != null) {
            if (topico.getVotosUteis().contains(usuario)) {
                topico.getVotosUteis().remove(usuario);
            } else {
                topico.getVotosUteis().add(usuario);
                if (!topico.getAutor().equals(usuario)) {
                    Notificacao notif = new Notificacao(topico.getAutor(), usuario,
                            "achou seu tópico útil: " + topico.getTitulo(), "/comunidade/topico/" + topico.getId());
                    notificacaoRepository.save(notif);
                }
            }
            topicoRepository.save(topico);
        }
        return "redirect:/comunidade/topico/" + id;
    }

    @PostMapping("/postagem/{id}/curtir")
    public String curtirPostagem(@PathVariable Long id, Principal principal) {
        if (principal == null)
            return "redirect:/login";
        Postagem post = postagemRepository.findById(id).orElse(null);
        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);

        if (post != null && usuario != null) {
            if (post.getCurtidas().contains(usuario)) {
                post.getCurtidas().remove(usuario);
            } else {
                post.getCurtidas().add(usuario);
                if (!post.getAutor().equals(usuario)) {
                    Notificacao notif = new Notificacao(post.getAutor(), usuario,
                            "curtiu sua resposta no tópico: " + post.getTopico().getTitulo(),
                            "/comunidade/topico/" + post.getTopico().getId());
                    notificacaoRepository.save(notif);
                }
            }
            postagemRepository.save(post);
        }
        return "redirect:/comunidade/topico/" + (post != null ? post.getTopico().getId() : "");
    }
}
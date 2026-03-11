package com.joao.tibia_scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarEmailConfirmacao(String destinatario, String token) {
        String linkConfirmacao = "http://localhost:8080/confirmar-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("TibiaBuilder - Confirmação de Registo");
        message.setText("Olá!\n\n" +
                "Obrigado por se registar no TibiaBuilder.\n" +
                "Para ativar a sua conta, por favor clique no link abaixo:\n\n" +
                linkConfirmacao + "\n\n" +
                "Se não foi você que solicitou, por favor ignore este email.");

        mailSender.send(message);
    }

    public void enviarEmailRecuperacaoSenha(String destinatario, String token) {
        String linkRecuperacao = "http://localhost:8080/resetar-senha?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("TibiaBuilder - Recuperação de Senha");
        message.setText("Olá!\n\n" +
                "Recebemos um pedido para redefinir a sua senha.\n" +
                "Para criar uma nova senha, clique no link abaixo:\n\n" +
                linkRecuperacao + "\n\n" +
                "Este link expira em 1 hora.\n" +
                "Se não foi você que solicitou, por favor ignore este email.");

        mailSender.send(message);
    }
}
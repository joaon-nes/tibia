package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;
    private String link;

    private boolean lida = false;
    private LocalDateTime data = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "usuario_destino_id")
    private Usuario destinatario;

    @ManyToOne
    @JoinColumn(name = "usuario_origem_id")
    private Usuario remetente;

    public Notificacao(Usuario destinatario, Usuario remetente, String mensagem, String link) {
        this.destinatario = destinatario;
        this.remetente = remetente;
        this.mensagem = mensagem;
        this.link = link;
    }

    public String getIcone() {
        if (mensagem.contains("curtiu"))
            return "fa-heart text-danger";
        if (mensagem.contains("útil"))
            return "fa-thumbs-up text-success";
        return "fa-comment text-info";
    }
}
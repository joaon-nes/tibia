package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Notificacao;
import com.joao.tibia_scrapper.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByDestinatarioOrderByDataDesc(Usuario destinatario);

    List<Notificacao> findTop5ByDestinatarioOrderByDataDesc(Usuario destinatario);

    long countByDestinatarioAndLidaFalse(Usuario destinatario);
}
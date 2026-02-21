package com.joao.tibia_scrapper.dto;

public record NotificacaoDTO(
        Long id,
        String mensagem,
        String remetente,
        String link,
        String icone,
        boolean lida,
        String dataFormatada) {
}
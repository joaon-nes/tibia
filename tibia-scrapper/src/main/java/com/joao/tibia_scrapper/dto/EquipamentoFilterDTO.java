package com.joao.tibia_scrapper.dto;

public record EquipamentoFilterDTO(
    String categoria,
    Integer level,
    String vocacao,
    String protecao,
    String elemento,
    String bonus,
    String atributos,
    String cargas,
    String duracao,
    Integer range,
    Integer slots,
    Integer tier,
    String mundo
) {}
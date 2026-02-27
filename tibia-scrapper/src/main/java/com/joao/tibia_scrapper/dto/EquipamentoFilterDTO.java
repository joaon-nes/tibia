package com.joao.tibia_scrapper.dto;

public record EquipamentoFilterDTO(
                String categoria,
                Integer level,
                String vocacao,
                String elemento,
                String protecao,
                String bonusEspecial,
                String atributos,
                Integer slots,
                Integer tier) {
}
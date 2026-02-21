package com.joao.tibia_scrapper.dto;

public record CharacterDTO(
        String name,
        String vocation,
        Integer level,
        String world,
        String residence,
        String sex) {
}
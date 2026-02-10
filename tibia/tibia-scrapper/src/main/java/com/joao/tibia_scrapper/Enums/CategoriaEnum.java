package com.joao.tibia_scrapper.Enums;

import lombok.Getter;

@Getter
public enum CategoriaEnum {

    HELMETS("Helmets", "wiki/Capacetes"),
    ARMORS("Armors", "wiki/Armaduras"),
    LEGS("Legs", "wiki/Calças"),
    BOOTS("Boots", "wiki/Botas"),

    SHIELDS("Shields", "wiki/Escudos"),
    SPELLBOOKS("Spellbooks", "wiki/Spellbooks"),

    SWORDS("Swords", "wiki/Espadas"),
    AXES("Axes", "wiki/Machados"),
    CLUBS("Clubs", "wiki/Clavas"),
    FIST("Fist", "wiki/Punhos"),

    WANDS("Wands", "wiki/Wands"),
    RODS("Rods", "wiki/Rods"),

    DISTANCE("Distance", "wiki/Distância"),
    AMMUNITION("Ammunition", "wiki/Munição"),
    QUIVERS("Quivers", "wiki/Aljavas"),

    AMULETS("Amulets", "wiki/Amuletos_e_Colares"),
    RINGS("Rings", "wiki/An%C3%A9is"),
    EXTRA_SLOT("Extra Slot", "wiki/Extra_Slot"),
    BACKPACKS("Backpacks", "wiki/Recipientes");

    private final String nome;
    private final String subUrl;

    CategoriaEnum(String nome, String subUrl) {
        this.nome = nome;
        this.subUrl = subUrl;
    }
}
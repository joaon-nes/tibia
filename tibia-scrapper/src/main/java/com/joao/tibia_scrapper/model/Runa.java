package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Runa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String imagemUrl; 
    
    private String spellWords;
    private String vocacoes;
    private String premium;
    private Integer levelMinimo;
    private Integer magicLevel;
    private Integer mana;
    private Integer soulPoints;

    @Column(columnDefinition = "TEXT")
    private String efeito;
}
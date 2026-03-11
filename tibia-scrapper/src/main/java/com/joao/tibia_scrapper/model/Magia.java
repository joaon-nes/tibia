package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Magia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String imagemUrl;
    private String grupo;

    private Integer levelMinimo;
    private String vocacoes;
    private String premium;
    private String spellWords;
    private Integer mana;

    @Column(columnDefinition = "TEXT")
    private String efeito;
}
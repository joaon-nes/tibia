package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Criatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;
    private String categoria;
    private String imagemUrl;

    // Stats
    private String hp;
    private String exp;
    private String speed;
    private String armor;
    private Integer charms;

    // Comportamento e Imunidades
    private String summon;
    private String pushobjects;
    private String senseinvis;
    private String runsat;
    private String paraimmune;
    private String pushable;
    private String imunidades;
    private String passaPor;

    // Modificadores de Dano
    private String physicalDmgMod;
    private String earthDmgMod;
    private String fireDmgMod;
    private String deathDmgMod;
    private String energyDmgMod;
    private String holyDmgMod;
    private String iceDmgMod;
    private String healMod;
    private String hpDrainDmgMod;
    private String drownDmgMod;

    // Textos longos
    @Column(columnDefinition = "TEXT")
    private String sounds;

    @Column(columnDefinition = "TEXT")
    private String abilities;

    @Column(columnDefinition = "TEXT")
    private String loot;

    @Column(columnDefinition = "TEXT")
    private String bestiarytext;

    @Column(columnDefinition = "TEXT")
    private String comportamento;

    @Column(columnDefinition = "TEXT")
    private String locaisFixos;

    @Column(columnDefinition = "TEXT")
    private String locaisInvasao;

    private String boss;
    private String bossDo;
}
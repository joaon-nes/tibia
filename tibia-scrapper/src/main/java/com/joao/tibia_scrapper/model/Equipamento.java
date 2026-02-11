package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String nome;
    private String categoria;

    @Column(name = "level_minimo")
    private Integer levelMinimo = 0;

    @Column(length = 500)
    private String vocacoes;

    private String maos;
    private Integer ataque = 0;
    private Integer defesa = 0;
    
    @Column(name = "mod_defesa")
    private String modDefesa;
    
    private Integer armadura = 0;
    private Integer alcance = 0;
    
    @Column(name = "hit_percent")
    private String hitPercent;

    @Column(name = "dano_elemental")
    private String danoElemental;

    @Column(name = "elemental_bond")
    private String elementalBond;
    
    private String protecao;
    
    @Column(name = "tipo_dano")
    private String tipoDano;
    
    @Column(name = "dano_medio")
    private String danoMedio;
    
    @Column(name = "mana_por_ataque")
    private Integer manaPorAtaque = 0;
    
    @Column(name = "bonus_skill")
    private String bonusSkill;

    @Column(name = "atributos")
    private String atributos;

    private Integer slots = 0;
    private Integer tier = 0;
    private Integer volume = 0;
    private String cargas;
    private String duracao;

    @Column(length = 500)
    private String imagemUrl;
    
    @Column(length = 500)
    private String wikiUrl;
}
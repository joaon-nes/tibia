package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;

@Entity
@Table(name = "equipamento")
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

    // public Equipamento() {}

    // --- GETTERS E SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getLevelMinimo() { return levelMinimo; }
    public void setLevelMinimo(Integer levelMinimo) { this.levelMinimo = levelMinimo; }

    public String getVocacoes() { return vocacoes; }
    public void setVocacoes(String vocacoes) { this.vocacoes = vocacoes; }

    public String getMaos() { return maos; }
    public void setMaos(String maos) { this.maos = maos; }

    public Integer getAtaque() { return ataque; }
    public void setAtaque(Integer ataque) { this.ataque = ataque; }

    public Integer getDefesa() { return defesa; }
    public void setDefesa(Integer defesa) { this.defesa = defesa; }

    public String getModDefesa() { return modDefesa; }
    public void setModDefesa(String modDefesa) { this.modDefesa = modDefesa; }

    public Integer getArmadura() { return armadura; }
    public void setArmadura(Integer armadura) { this.armadura = armadura; }

    public Integer getAlcance() { return alcance; }
    public void setAlcance(Integer alcance) { this.alcance = alcance; }

    public String getHitPercent() { return hitPercent; }
    public void setHitPercent(String hitPercent) { this.hitPercent = hitPercent; }

    public String getDanoElemental() { return danoElemental; }
    public void setDanoElemental(String danoElemental) { this.danoElemental = danoElemental; }

    public String getElementalBond() { return elementalBond; }
    public void setElementalBond(String elementalBond) { this.danoElemental = elementalBond; }

    public String getProtecao() { return protecao; }
    public void setProtecao(String protecao) { this.protecao = protecao; }

    public String getTipoDano() { return tipoDano; }
    public void setTipoDano(String tipoDano) { this.tipoDano = tipoDano; }

    public String getDanoMedio() { return danoMedio; }
    public void setDanoMedio(String danoMedio) { this.danoMedio = danoMedio; }

    public Integer getManaPorAtaque() { return manaPorAtaque; }
    public void setManaPorAtaque(Integer manaPorAtaque) { this.manaPorAtaque = manaPorAtaque; }

    public String getBonusSkill() { return bonusSkill; }
    public void setBonusSkill(String bonusSkill) { this.bonusSkill = bonusSkill; }

    public String getAtributos() { return atributos; }
    public void setAtributos(String atributos) { this.atributos = atributos; }

    public Integer getSlots() { return slots; }
    public void setSlots(Integer slots) { this.slots = slots; }

    public Integer getTier() { return tier; }
    public void setTier(Integer tier) { this.tier = tier; }

    public Integer getVolume() { return volume; }
    public void setVolume(Integer volume) { this.volume = volume; }

    public String getCargas() { return cargas; }
    public void setCargas(String cargas) { this.cargas = cargas; }

    public String getDuracao() { return duracao; }
    public void setDuracao(String duracao) { this.duracao = duracao; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public String getWikiUrl() { return wikiUrl; }
    public void setWikiUrl(String wikiUrl) { this.wikiUrl = wikiUrl; }
}
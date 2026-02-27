package com.joao.tibia_scrapper.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String categoria;
    private String imagemUrl;

    private Integer levelMinimo;
    private String vocacoes;
    private String maos;

    private Integer defesa;
    private String modDefesa;
    private Integer ataque;
    private Integer armadura;

    private Integer slots;
    private Integer tier;

    private String atributos;
    private String danoElemental;

    private Integer lifeLeechChance;
    private Integer lifeLeechAmount;

    private Integer manaLeechChance;
    private Integer manaLeechAmount;

    private Integer criticalChance;
    private Integer criticalDamage;

    private Integer mantra;

    private Integer alcance;

    private String protecao;

    private String elementalBond;

    // --- GETTERS E SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public Integer getLevelMinimo() {
        return levelMinimo;
    }

    public void setLevelMinimo(Integer levelMinimo) {
        this.levelMinimo = levelMinimo;
    }

    public String getVocacoes() {
        return vocacoes;
    }

    public void setVocacoes(String vocacoes) {
        this.vocacoes = vocacoes;
    }

    public String getMaos() {
        return maos;
    }

    public void setMaos(String maos) {
        this.maos = maos;
    }

    public Integer getDefesa() {
        return defesa;
    }

    public void setDefesa(Integer defesa) {
        this.defesa = defesa;
    }

    public String getModDefesa() {
        return modDefesa;
    }

    public void setModDefesa(String modDefesa) {
        this.modDefesa = modDefesa;
    }

    public Integer getAtaque() {
        return ataque;
    }

    public void setAtaque(Integer ataque) {
        this.ataque = ataque;
    }

    public Integer getArmadura() {
        return armadura;
    }

    public void setArmadura(Integer armadura) {
        this.armadura = armadura;
    }

    public Integer getSlots() {
        return slots;
    }

    public void setSlots(Integer slots) {
        this.slots = slots;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    public String getAtributos() {
        return atributos;
    }

    public void setAtributos(String atributos) {
        this.atributos = atributos;
    }

    public String getDanoElemental() {
        return danoElemental;
    }

    public void setDanoElemental(String danoElemental) {
        this.danoElemental = danoElemental;
    }

    public Integer getLifeLeechChance() {
        return lifeLeechChance;
    }

    public void setLifeLeechChance(Integer lifeLeechChance) {
        this.lifeLeechChance = lifeLeechChance;
    }

    public Integer getLifeLeechAmount() {
        return lifeLeechAmount;
    }

    public void setLifeLeechAmount(Integer lifeLeechAmount) {
        this.lifeLeechAmount = lifeLeechAmount;
    }

    public Integer getManaLeechChance() {
        return manaLeechChance;
    }

    public void setManaLeechChance(Integer manaLeechChance) {
        this.manaLeechChance = manaLeechChance;
    }

    public Integer getManaLeechAmount() {
        return manaLeechAmount;
    }

    public void setManaLeechAmount(Integer manaLeechAmount) {
        this.manaLeechAmount = manaLeechAmount;
    }

    public Integer getCriticalChance() {
        return criticalChance;
    }

    public void setCriticalChance(Integer criticalChance) {
        this.criticalChance = criticalChance;
    }

    public Integer getCriticalDamage() {
        return criticalDamage;
    }

    public void setCriticalDamage(Integer criticalDamage) {
        this.criticalDamage = criticalDamage;
    }

    public Integer getMantra() {
        return mantra;
    }

    public void setMantra(Integer mantra) {
        this.mantra = mantra;
    }

    public Integer getAlcance() {
        return alcance;
    }

    public void setAlcance(Integer alcance) {
        this.alcance = alcance;
    }

    public String getProtecao() {
        return protecao;
    }

    public void setProtecao(String protecao) {
        this.protecao = protecao;
    }

    public String getElementalBond() {
        return elementalBond;
    }

    public void setElementalBond(String elementalBond) {
        this.elementalBond = elementalBond;
    }
}
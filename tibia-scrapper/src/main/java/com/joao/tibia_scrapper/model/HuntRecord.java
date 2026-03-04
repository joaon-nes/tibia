package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class HuntRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String sessionStart;
    private String sessionLength;
    private String xpGain;
    private String loot;
    private String supplies;
    private String balance;
    private boolean isWaste;

    @Column(columnDefinition = "LONGTEXT")
    private String huntJsonOriginal;

    @Column(columnDefinition = "LONGTEXT")
    private String partySnapshot;

    @Column(unique = true, length = 15)
    private String slug;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime dataRegistro = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(String sessionStart) {
        this.sessionStart = sessionStart;
    }

    public String getSessionLength() {
        return sessionLength;
    }

    public void setSessionLength(String sessionLength) {
        this.sessionLength = sessionLength;
    }

    public String getXpGain() {
        return xpGain;
    }

    public void setXpGain(String xpGain) {
        this.xpGain = xpGain;
    }

    public String getLoot() {
        return loot;
    }

    public void setLoot(String loot) {
        this.loot = loot;
    }

    public String getSupplies() {
        return supplies;
    }

    public void setSupplies(String supplies) {
        this.supplies = supplies;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public boolean isWaste() {
        return isWaste;
    }

    public void setWaste(boolean waste) {
        isWaste = waste;
    }

    public String getHuntJsonOriginal() {
        return huntJsonOriginal;
    }

    public void setHuntJsonOriginal(String huntJsonOriginal) {
        this.huntJsonOriginal = huntJsonOriginal;
    }

    public String getPartySnapshot() {
        return partySnapshot;
    }

    public void setPartySnapshot(String partySnapshot) {
        this.partySnapshot = partySnapshot;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
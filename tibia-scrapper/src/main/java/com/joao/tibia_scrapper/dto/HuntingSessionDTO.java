package com.joao.tibia_scrapper.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HuntingSessionDTO {

    @JsonProperty("SessionStart")
    @JsonAlias({ "Session start" })
    private String sessionStart;

    @JsonProperty("SessionEnd")
    @JsonAlias({ "Session end" })
    private String sessionEnd;

    @JsonProperty("SessionLength")
    @JsonAlias({ "Session length" })
    private String sessionLength;

    @JsonProperty("XPGain")
    @JsonAlias({ "XP Gain" })
    private String xpGain;

    @JsonProperty("XPGainHour")
    @JsonAlias({ "XP/h" })
    private String xpGainHour;

    @JsonProperty("RawXPGain")
    @JsonAlias({ "Raw XP Gain" })
    private String rawXpGain;

    @JsonProperty("RawXPGainHour")
    @JsonAlias({ "Raw XP/h" })
    private String rawXpGainHour;

    @JsonProperty("Loot")
    private String loot;

    @JsonProperty("Supplies")
    private String supplies;

    @JsonProperty("Balance")
    private String balance;

    @JsonProperty("Damage")
    private String damage;

    @JsonProperty("DamageHour")
    @JsonAlias({ "Damage/h" })
    private String damageHour;

    @JsonProperty("Healing")
    private String healing;

    @JsonProperty("HealingHour")
    @JsonAlias({ "Healing/h" })
    private String healingHour;

    @JsonProperty("KilledMonsters")
    @JsonAlias({ "Killed Monsters" })
    private List<HuntingEntryDTO> killedMonsters;

    @JsonProperty("LootedItems")
    @JsonAlias({ "Looted Items" })
    private List<HuntingEntryDTO> lootedItems;

    // Getters and Setters
    public String getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(String sessionStart) {
        this.sessionStart = sessionStart;
    }

    public String getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(String sessionEnd) {
        this.sessionEnd = sessionEnd;
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

    public String getXpGainHour() {
        return xpGainHour;
    }

    public void setXpGainHour(String xpGainHour) {
        this.xpGainHour = xpGainHour;
    }

    public String getRawXpGain() {
        return rawXpGain;
    }

    public void setRawXpGain(String rawXpGain) {
        this.rawXpGain = rawXpGain;
    }

    public String getRawXpGainHour() {
        return rawXpGainHour;
    }

    public void setRawXpGainHour(String rawXpGainHour) {
        this.rawXpGainHour = rawXpGainHour;
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

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getDamageHour() {
        return damageHour;
    }

    public void setDamageHour(String damageHour) {
        this.damageHour = damageHour;
    }

    public String getHealing() {
        return healing;
    }

    public void setHealing(String healing) {
        this.healing = healing;
    }

    public String getHealingHour() {
        return healingHour;
    }

    public void setHealingHour(String healingHour) {
        this.healingHour = healingHour;
    }

    public List<HuntingEntryDTO> getKilledMonsters() {
        return killedMonsters;
    }

    public void setKilledMonsters(List<HuntingEntryDTO> killedMonsters) {
        this.killedMonsters = killedMonsters;
    }

    public List<HuntingEntryDTO> getLootedItems() {
        return lootedItems;
    }

    public void setLootedItems(List<HuntingEntryDTO> lootedItems) {
        this.lootedItems = lootedItems;
    }
}
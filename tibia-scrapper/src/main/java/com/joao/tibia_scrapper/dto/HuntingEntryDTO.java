package com.joao.tibia_scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HuntingEntryDTO {
    @JsonProperty("Count")
    private Integer count;

    @JsonProperty("Name")
    private String name;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
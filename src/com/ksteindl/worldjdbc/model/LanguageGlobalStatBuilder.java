package com.ksteindl.worldjdbc.model;

public class LanguageGlobalStatBuilder {
    private String name;
    private Long globalSpeakers;
    private Integer countryCount;

    public LanguageGlobalStatBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public LanguageGlobalStatBuilder setGlobalSpeakers(Long globalSpeakers) {
        this.globalSpeakers = globalSpeakers;
        return this;
    }

    public LanguageGlobalStatBuilder setCountryCount(Integer countryCount) {
        this.countryCount = countryCount;
        return this;
    }

    public LanguageGlobalStat createLanguageGlobalStat() {
        return new LanguageGlobalStat(name, globalSpeakers, countryCount);
    }
}
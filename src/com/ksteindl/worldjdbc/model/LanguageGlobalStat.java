package com.ksteindl.worldjdbc.model;

public class LanguageGlobalStat implements Printable{

    private String name;
    private Long globalSpeakers;
    private Integer countryCount;

    public LanguageGlobalStat(String name, Long globalSpeakers, Integer countryCount) {
        this.name = name;
        this.globalSpeakers = globalSpeakers;
        this.countryCount = countryCount;
    }

    public String getName() {
        return name;
    }

    public Long getGlobalSpeakers() {
        return globalSpeakers;
    }

    public Integer getCountryCount() {
        return countryCount;
    }
}

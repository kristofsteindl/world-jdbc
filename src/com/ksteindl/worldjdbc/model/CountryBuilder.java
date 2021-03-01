package com.ksteindl.worldjdbc.model;

import java.util.List;

public class CountryBuilder {
    private String name;
    private String code;
    private String continent;
    private Long population;
    private Double gnp;
    private City capital;
    private List<Language> languages;

    public CountryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CountryBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    public CountryBuilder setContinent(String continent) {
        this.continent = continent;
        return this;
    }

    public CountryBuilder setPopulation(Long population) {
        this.population = population;
        return this;
    }

    public CountryBuilder setGnp(Double gnp) {
        this.gnp = gnp;
        return this;
    }

    public CountryBuilder setCapital(City capital) {
        this.capital = capital;
        return this;
    }

    public CountryBuilder setLanguages(List<Language> languages) {
        this.languages = languages;
        return this;
    }


    public Country createCountry() {
        return new Country(name, code, continent, population, gnp, capital, languages);
    }
}
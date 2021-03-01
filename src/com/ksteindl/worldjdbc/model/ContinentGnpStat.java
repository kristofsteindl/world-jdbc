package com.ksteindl.worldjdbc.model;

public class ContinentGnpStat implements Printable{

    private String name;
    private Double millionGnp;
    private Long population;

    public ContinentGnpStat(String name, Double gnpPerCapita, Long population) {
        this.name = name;
        this.millionGnp = gnpPerCapita;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public Double getMillionGnp() {
        return millionGnp;
    }

    public Long getPopulation() {
        return population;
    }
}

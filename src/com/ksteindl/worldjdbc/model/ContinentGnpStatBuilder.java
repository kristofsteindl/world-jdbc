package com.ksteindl.worldjdbc.model;

public class ContinentGnpStatBuilder {
    private String name;
    private Double millionGnp;
    private Long population;

    public ContinentGnpStatBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ContinentGnpStatBuilder setMillionGnp(Double millionGnp) {
        this.millionGnp = millionGnp;
        return this;
    }

    public ContinentGnpStatBuilder setPopulation(Long population) {
        this.population = population;
        return this;
    }

    public ContinentGnpStat createContinentGnpStat() {
        return new ContinentGnpStat(name, millionGnp, population);
    }
}
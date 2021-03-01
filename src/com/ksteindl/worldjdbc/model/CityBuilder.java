package com.ksteindl.worldjdbc.model;

public class CityBuilder {
    private Long ID;
    private String name;
    private Long population;
    private String district;
    private Country country;

    public CityBuilder() {
    }

    public CityBuilder(City city) {
        this.ID = city.getID();
        this.name = city.getName();
        this.population = city.getPopulation();
        this.district = city.getDistrict();
        this.country = city.getCountry();
    }

    public CityBuilder setID(Long ID) {
        this.ID = ID;
        return this;
    }

    public CityBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CityBuilder setPopulation(Long population) {
        this.population = population;
        return this;
    }

    public CityBuilder setDistrict(String district) {
        this.district = district;
        return this;
    }

    public CityBuilder setCountry(Country country) {
        this.country = country;
        return this;
    }

    public Long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Long getPopulation() {
        return population;
    }

    public String getDistrict() {
        return district;
    }

    public Country getCountry() {
        return country;
    }

    public City createCity() {
        return new City(ID, name, population, district, country);
    }
}
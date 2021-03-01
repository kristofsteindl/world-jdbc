package com.ksteindl.worldjdbc.model;

import java.util.List;

public class Country implements Printable{

	private String name;
	private String code;
	private String continent;
	private Long population;
	private Double gnp;
	private City capital;
	private List<Language> languages;


	// Not a full representation of a 'country' table row, because of the application specification
	public Country(String name, String code, String continent, Long population, Double gnp, City capital, List<Language> languages) {
		super();
		this.name = name;
		this.code = code;
		this.continent = continent;
		this.population = population;
		this.gnp = gnp;
		this.capital = capital;
		this.languages = languages;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getContinent() {
		return continent;
	}

	public Long getPopulation() {
		return population;
	}

	public Double getGnp() {
		return gnp;
	}

	public City getCapital() {
		return capital;
	}

	public List<Language> getLanguages() {
		return languages;
	}
}

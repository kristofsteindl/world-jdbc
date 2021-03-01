package com.ksteindl.worldjdbc.model;

public class City implements Printable{
	
	private Long ID;
	private String name;
	private Long population;
	private String district;
	private Country country;
	
	public City(Long iD, String name, Long population, String district, Country country) {
		super();
		ID = iD;
		this.name = name;
		this.population = population;
		this.district = district;
		this.country = country;
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

	@Override
	public String toString() {
		return "City{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", population=" + population +
				", country=" + country +
				'}';
	}
}

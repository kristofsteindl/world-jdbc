package com.ksteindl.worldjdbc.option.select;

import com.ksteindl.worldjdbc.dao.CountryDAO;
import com.ksteindl.worldjdbc.model.ContinentGnpStat;
import com.ksteindl.worldjdbc.model.ContinentGnpStatBuilder;
import com.ksteindl.worldjdbc.model.Country;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ContinentGnpOption extends Option {

	/* 	UI and service functionality is mixed here, a new Service layer could be introduced between DAO and Option layer.
	* 	However, this class doesn't consist much data processing and validation business logic, so that would be unnecessary over engineering.
	* */

	private static final String PROMPT_MESSAGE = "List GNP per capita of continents";
	private static final Integer MINIMUM_HABITANT = 1;
	private static final Long MILLION = 1_000_000l;

	private final CountryDAO countryDAO;

	public ContinentGnpOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.countryDAO = CountryDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		try {
			List<Country> countries = countryDAO.getCountriesWithGnp();
			Map<String, List<Country>> countriesOfContinents = countries.stream().collect(Collectors.groupingBy(Country::getContinent));
			List<ContinentGnpStat> continentGnpStats = getContinentGnpStats(countriesOfContinents);
			System.out.println("The GNP per capita value of the continents:");
			PrintUtils.printResults(continentGnpStats, this::getContinentGnpStatLine);
			System.out.println("World average: " + round(continentGnpStats.stream().mapToDouble(continent -> continent.getMillionGnp()).sum() / continentGnpStats.stream().mapToDouble(continent -> continent.getPopulation()).sum() * MILLION, 1) + "\n\n");
			return true;
		} catch (SQLException sqlException) {
			System.out.println("Something went wrong");
			System.out.println(sqlException.getMessage());
		}
		return false;
	}

	private String getContinentGnpStatLine(ContinentGnpStat continentGnpStat) {
		StringBuilder builder = new StringBuilder()
				.append(continentGnpStat.getName())
				.append(" GNP per capita value is ")
				.append(round(continentGnpStat.getMillionGnp() / continentGnpStat.getPopulation() * MILLION, 1));
		return builder.toString();
	}

	private List<ContinentGnpStat> getContinentGnpStats(Map<String, List<Country>> countriesOfContinents) {
		return countriesOfContinents.entrySet().stream()
				.filter(entry -> entry.getValue().stream().mapToLong(country -> country.getPopulation()).sum() >= MINIMUM_HABITANT)
				.map(entry -> createContinentGnpStat(entry))
				//.sorted(Comparator.comparing(ContinentGnpStat::getGnp).reversed())
				.sorted((continent1, continent2) -> continent1.getMillionGnp() / continent1.getPopulation() < continent2.getMillionGnp() / continent2.getPopulation() ? 1 : -1)
				.collect(Collectors.toList());
	}

	private ContinentGnpStat createContinentGnpStat(Map.Entry<String, List<Country>> entry) {
		List<Country> countries = entry.getValue();
		Long sumPopulation = countries.stream().mapToLong(country -> country.getPopulation()).sum();
		Double sumGnp = countries.stream().mapToDouble(country -> country.getGnp()).sum();
		return new ContinentGnpStatBuilder()
				.setName(entry.getKey())
				.setMillionGnp(sumGnp)
				.setPopulation(sumPopulation)
				.createContinentGnpStat();
	}

	private Double calculateWeightedAverageGnp(List<Country> countries) {
		Long sumPopulation = countries.stream().mapToLong(country -> country.getPopulation()).sum();
		Double sumGnp = countries.stream().mapToDouble(country -> country.getPopulation() * country.getGnp()).sum();
		return round(sumGnp / sumPopulation, 1);
	}

	private Double round(Double number, Integer decimal) {
		if (decimal < 0) {
			return number;
		}
		Double h = Math.pow(10, decimal);
		return Math.round(number * h) / h;
	}
}

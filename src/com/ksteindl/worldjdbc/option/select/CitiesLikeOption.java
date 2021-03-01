package com.ksteindl.worldjdbc.option.select;

import com.ksteindl.worldjdbc.dao.CityDAO;
import com.ksteindl.worldjdbc.model.City;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CitiesLikeOption extends Option {

	private static final String PROMPT_MESSAGE = "Give an existing city, or a part of an existing city using '*' as wildcard character";

	private final CityDAO cityDAO;

	public CitiesLikeOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.cityDAO = CityDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		String input = scanner.next();
		try {
			String sqlizedInput = input.replace('*', '%');
			List<City> cities = cityDAO.getCitiesLike(sqlizedInput);
			if (cities.size() == 0) {
				System.out.println("No city was found in the database for query word " + input + ".");
				return false;
			} else {
				System.out.println("The cities for query word " + input + " in alphabetic order");
				PrintUtils.printResults(cities, this::getCityOfCountryLine);
			}
		} catch (SQLException sqlException) {
			System.out.println("Something went wrong");
			System.out.println(sqlException.getMessage());
		}
		return false;

	}


	private String getCityOfCountryLine(City city) {
		StringBuilder builder = new StringBuilder()
				.append(city.getName())
				.append(city.getID().equals(city.getCountry().getCapital().getID()) ? ", capital of " : ", ")
				.append(city.getCountry().getName())
				.append(", population ratio against the country: ")
				.append(((double)(city.getPopulation() * 1000 / city.getCountry().getPopulation())) / 10)
				.append("%, population: ")
				.append(city.getPopulation());
		return builder.toString();
	}
}

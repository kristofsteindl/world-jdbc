package com.ksteindl.worldjdbc.option.select;

import com.ksteindl.worldjdbc.dao.CityDAO;
import com.ksteindl.worldjdbc.model.City;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CitiesOfCountryOption extends Option {

	private static final String PROMPT_MESSAGE = "Give an existing country";

	private final CityDAO cityDAO;

	public CitiesOfCountryOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.cityDAO = CityDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		String input = scanner.next();
		try {
			List<City> cities = cityDAO.getCitiesInCountry(input);
			if (cities.size() == 0) {
				System.out.println("No city was found in the database for country " + input + ". Maybe the country's official name is something else.");
				return false;
			} else {
				System.out.println("The cities of " + input + " in a reverse order of population.");
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
				.append(", population: ")
				.append(city.getPopulation())
				.append(", district: ")
				.append(city.getDistrict())
				.append(" (ID: ")
				.append(city.getID())
				.append(")");
		return builder.toString();
	}
}

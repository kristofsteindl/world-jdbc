package com.ksteindl.worldjdbc.option.select;

import com.ksteindl.worldjdbc.dao.CountryDAO;
import com.ksteindl.worldjdbc.model.Country;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CountriesOfContinentOption extends Option {

	private static final String PROMPT_MESSAGE = "Give an existing continent, or the part of an existing continent using '*' character as a wildcard";

	private final CountryDAO countryDAO;

	public CountriesOfContinentOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.countryDAO = CountryDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		String input = scanner.next();
		try {
			String sqlizedInput = input.replace('*', '%');
			List<Country> countries = countryDAO.getCountriesOfContinents(sqlizedInput);
			if (countries.size() == 0) {
				System.out.println("No country was found in the database for continent query word " + input + ".");
				return false;
			} else {
				System.out.println("The countries of " + input + " query word are (in ascending order of continent name and country):");
				PrintUtils.printResults(countries, this::getCountryOfContinentLine);
			}
		} catch (SQLException sqlException) {
			System.out.println("Something went wrong");
			System.out.println(sqlException.getMessage());
		}
		return false;

	}

	private String getCountryOfContinentLine(Country country) {
		StringBuilder builder = new StringBuilder()
				.append(country.getContinent())
				.append(", ")
				.append(country.getName())
				.append(", capital: ")
				.append(country.getCapital().getName());
		return builder.toString();
	}
}

package com.ksteindl.worldjdbc.option.select;

import com.ksteindl.worldjdbc.dao.CountryDAO;
import com.ksteindl.worldjdbc.model.Country;
import com.ksteindl.worldjdbc.model.Language;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CountriesOfLanguageOption extends Option {

	private static final String PROMPT_MESSAGE = "Give an existing language";

	private final CountryDAO countryDAO;

	public CountriesOfLanguageOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.countryDAO = CountryDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		String input = scanner.next();
		try {
			List<Country> countries = countryDAO.getCountriesOfLanguage(input);
			if (countries.size() == 0) {
				System.out.println("No country was found in the database for language " + input + ". Maybe it is written in another way.");
				return false;
			} else {
				System.out.println("The " + input + " speaking countries are");
				PrintUtils.printResults(countries, this::getCountryOfLanguageLine);
			}
		} catch (SQLException sqlException) {
			System.out.println("Something went wrong");
			System.out.println(sqlException.getMessage());
		}
		return false;

	}

	private String getCountryOfLanguageLine(Country country) {
		assert country.getLanguages().size() == 1;
		Language language = country.getLanguages().get(0);
		StringBuilder builder = new StringBuilder()
				.append("In ")
				.append(country.getName())
				.append(" ")
				.append(language.getLanguage())
				.append(" is ")
				.append(language.getOfficial() ? " " : "not ")
				.append("official, and the mother tongue of ")
				.append((int) (language.getPercentage() * country.getPopulation()))
				.append(" people.");
		return builder.toString();
	}
}

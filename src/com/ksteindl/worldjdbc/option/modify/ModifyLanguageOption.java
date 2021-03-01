package com.ksteindl.worldjdbc.option.modify;

import com.ksteindl.worldjdbc.dao.CountryDAO;
import com.ksteindl.worldjdbc.dao.LanguageDAO;
import com.ksteindl.worldjdbc.model.Country;
import com.ksteindl.worldjdbc.model.Language;
import com.ksteindl.worldjdbc.model.LanguageBuilder;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.Scanner;

public class ModifyLanguageOption extends Option {

	private static final String PROMPT_MESSAGE = "Give language, then the country, one by one";
	private static final String SUCCESS_MESSAGE = "Adding new language was successful!\nWARNING! The other language percentages weren't modifyed. You should probably want to alter the other language percentages of the country";

	private final LanguageDAO languageDAO;
	private final CountryDAO countryDAO;

	public ModifyLanguageOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.countryDAO = CountryDAO.getInstance();
		this.languageDAO = LanguageDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		try {
			Language existingLanguage = promptLanguage();
			LanguageBuilder languageBuilder = new LanguageBuilder(existingLanguage);
			appendIsOfficial(languageBuilder);
			appendPercentage(languageBuilder);
			boolean success = languageDAO.modifyLanguage(languageBuilder.createLanguage());
			if (success) {
				PrintUtils.printSuccesOperation(SUCCESS_MESSAGE);
			} else {
				System.out.println("Inserting into Database was not successfully because of unknown reason");
			}
			return success;
		} catch (SQLException sqlException) {
			System.out.println("Something went wrong");
			System.out.println(sqlException.getMessage());
		}
		return false;
	}

	private Language promptLanguage() throws SQLException {
		System.out.println("Language");
		String languageName = scanner.next();
		System.out.println("Country name");
		String countryName = scanner.next();
		Country country = countryDAO.getCountry(countryName);
		if (country == null) {
			throw new ModifyValidationException("Country " + countryName + " not found");
		}
		Language existingLanguage = languageDAO.getLanguagesByCountryCodeAndLanguage(country.getCode(), languageName);
		if (existingLanguage == null) {
			throw new ModifyValidationException("Language " + languageName + " with country " + countryName + " doesn't exists");
		}
		return existingLanguage;
	}

	private void appendIsOfficial(LanguageBuilder languageBuilder) {
		String YES = "yes";
		System.out.println(String.format("Is %s is official? ('%s' for yes, anything else for no, leave empty for the original value)", languageBuilder.getLanguage(), YES));
		String official = scanner.next();
		if (!"".equals(official)) {
			languageBuilder.setIsOfficial(YES.equals(official));
		}
	}

	private void appendPercentage(LanguageBuilder languageBuilderr) {
		System.out.println("Percentage");
		String percentage = scanner.next();
		Double percentageNumber;
		try {
			percentageNumber = Double.parseDouble(percentage);
			if (percentageNumber < 0 || percentageNumber > 100) {
				throw new ModifyValidationException("Percentage must be between 0.0 and 100.0!");
			}
		} catch (NumberFormatException numberFormatException) {
			throw new ModifyValidationException("Percentage must be a valid number!");
		}
		languageBuilderr.setPercentage(percentageNumber);
	}
}

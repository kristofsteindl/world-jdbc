package com.ksteindl.worldjdbc.option.modify;

import com.ksteindl.worldjdbc.dao.CountryDAO;
import com.ksteindl.worldjdbc.dao.LanguageDAO;
import com.ksteindl.worldjdbc.model.Country;
import com.ksteindl.worldjdbc.model.Language;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.Scanner;

public class DeleteLanguageOption extends Option {

	private static final String PROMPT_MESSAGE = "Delete existing language by language name and country";

	private static final String SUCCESS_MESSAGE = "Deleting %s from %s was successful!";

	private final LanguageDAO languageDAO;
	private final CountryDAO countryDAO;

	public DeleteLanguageOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.languageDAO = LanguageDAO.getInstance();
		this.countryDAO = CountryDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		try {
			Language existingLanguage = promptLanguage();
			boolean success = languageDAO.deleteLanguage(existingLanguage);
			if (success) {
				PrintUtils.printSuccesOperation(String.format(SUCCESS_MESSAGE, existingLanguage.getLanguage(), existingLanguage.getCountryCode()));
			} else {
				System.out.println("Deleting language " +existingLanguage.getLanguage() + " (" + existingLanguage.getCountryCode() + ") was not successfully because of unknown reason");
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


}

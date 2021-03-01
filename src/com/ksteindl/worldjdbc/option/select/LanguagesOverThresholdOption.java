package com.ksteindl.worldjdbc.option.select;

import com.ksteindl.worldjdbc.dao.LanguageDAO;
import com.ksteindl.worldjdbc.model.LanguageGlobalStat;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LanguagesOverThresholdOption extends Option {

	private static final String PROMPT_MESSAGE = "Give a treshold for language speakers";

	private final LanguageDAO languageDAO;

	public LanguagesOverThresholdOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.languageDAO = LanguageDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		try {
			String input = scanner.next();
			List<LanguageGlobalStat> languages = languageDAO.getLanguagesOverTreshold(Long.parseLong(input));
			if (languages.size() == 0) {
				System.out.println("There is no language in this database, that is spoken by more than " + input + " people");
				return false;
			} else {
				System.out.println("The languages that is spoken by more than " + input);
				PrintUtils.printResults(languages, this::getLanguageOverTresholdLine);
			}

		} catch (SQLException sqlException) {
			System.out.println("Something went wrong");
			System.out.println(sqlException.getMessage());
		} catch (NumberFormatException numberFormatException) {
			System.out.println("Error: you should give an integer number");
		}
		return false;
	}

	private String getLanguageOverTresholdLine(LanguageGlobalStat languageGlobalStat) {
		StringBuilder builder = new StringBuilder()
				.append(languageGlobalStat.getGlobalSpeakers())
				.append(" speak ")
				.append(languageGlobalStat.getName())
				.append(" in ")
				.append(languageGlobalStat.getCountryCount())
				.append(" country(s)");
		return builder.toString();
	}
}

package com.ksteindl.worldjdbc.option.modify;

import com.ksteindl.worldjdbc.dao.CityDAO;
import com.ksteindl.worldjdbc.model.City;
import com.ksteindl.worldjdbc.model.CityBuilder;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.Scanner;

public class ModifyCityOption extends Option {

	private static final String PROMPT_MESSAGE = "Give the ID of the existing city (if not sure, list by country)";
	private static final String PROMPT_MODIFY_PROPERTY = "%s (current: %s. Leave blank, if doesn't want to change)";
	private static final String SUCCESS_MESSAGE = "City modifying was successful! List cities of country to check!";

	private final CityDAO cityDAO;

	public ModifyCityOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.cityDAO = CityDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		try {
			City existingCity = promptCity();
			CityBuilder cityBuilder = new CityBuilder(existingCity);
			appendPopulation(cityBuilder);
			appendDistrict(cityBuilder);
			boolean success = cityDAO.modifyCity(cityBuilder.createCity());
			if (success) {
				PrintUtils.printSuccesOperation(SUCCESS_MESSAGE);
			} else {
				System.out.println("Modifying city " + cityBuilder.getName() + " was not successfully because of unknown reason");
			}
			return success;
		} catch (SQLException sqlException) {
			System.out.println("Something went wrong");
			System.out.println(sqlException.getMessage());
		}
		return false;
	}

	private City promptCity() throws SQLException {
		System.out.println("ID");
		String idInput = scanner.next();
		City existingCity;
		try {
			Long ID = Long.parseLong(idInput);
			existingCity = cityDAO.getCity(ID);
			if (existingCity == null) {
				throw new ModifyValidationException("City was not found with id " + idInput);
			}
			return existingCity;
		} catch (NumberFormatException numberFormatException) {
			throw new ModifyValidationException("City population must be a positive integer number!");
		}
	}

	private void appendDistrict(CityBuilder cityBuilder) {
		System.out.println(String.format(PROMPT_MODIFY_PROPERTY, "District", cityBuilder.getDistrict()));
		String district = scanner.next();
		cityBuilder.setDistrict(district);
	}

	private void appendPopulation(CityBuilder cityBuilder) {
		System.out.println(String.format(PROMPT_MODIFY_PROPERTY, "Population", cityBuilder.getPopulation().toString()));
		String population = scanner.next();
		Long populationNumber;
		try {
			populationNumber = Long.parseLong(population);
			if (populationNumber < 0) {
				throw new ModifyValidationException("City population must be a positive integer number!");
			}
		} catch (NumberFormatException numberFormatException) {
			throw new ModifyValidationException("City population must be a positive integer number!");
		}
		cityBuilder.setPopulation(populationNumber);
	}

}

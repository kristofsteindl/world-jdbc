package com.ksteindl.worldjdbc.option.modify;

import com.ksteindl.worldjdbc.dao.CityDAO;
import com.ksteindl.worldjdbc.dao.CountryDAO;
import com.ksteindl.worldjdbc.model.CityBuilder;
import com.ksteindl.worldjdbc.model.Country;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.Scanner;

public class CreateCityOption extends Option {

	private static final String PROMPT_MESSAGE = "Give the city attributes, one by one";
	private static final String SUCCESS_MESSAGE = "New city creation was successful! List cities of country to check!";

	private final CityDAO cityDAO;
	private final CountryDAO countryDAO;

	public CreateCityOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.cityDAO = CityDAO.getInstance();
		this.countryDAO = CountryDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		try {
			CityBuilder cityBuilder = new CityBuilder();
			appendName(cityBuilder);
			appendCountry(cityBuilder);
			appendDistrict(cityBuilder);
			appendPopulation(cityBuilder);

			boolean success = cityDAO.insertCity(cityBuilder.createCity());
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

	private void appendName(CityBuilder cityBuilder) {
		System.out.println("Name");
		String name = scanner.next();
		cityBuilder.setName(name);
	}

	private void appendCountry(CityBuilder cityBuilder) throws SQLException {
		System.out.println("Country name");
		String countryName = scanner.next();
		Country country = countryDAO.getCountry(countryName);
		if (country == null) {
			throw new ModifyValidationException("Country " + countryName + " not found");
		}
		cityBuilder.setCountry(country);
	}

	private void appendDistrict(CityBuilder cityBuilder) {
		System.out.println("District");
		String district = scanner.next();
		cityBuilder.setDistrict(district);
	}

	private void appendPopulation(CityBuilder cityBuilder) {
		System.out.println("Population");
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

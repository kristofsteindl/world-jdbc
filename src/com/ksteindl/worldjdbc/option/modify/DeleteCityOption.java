package com.ksteindl.worldjdbc.option.modify;

import com.ksteindl.worldjdbc.dao.CityDAO;
import com.ksteindl.worldjdbc.model.City;
import com.ksteindl.worldjdbc.option.Option;
import com.ksteindl.worldjdbc.util.PrintUtils;

import java.sql.SQLException;
import java.util.Scanner;

public class DeleteCityOption extends Option {

	private static final String PROMPT_MESSAGE = "Delete existing city by ID (if not sure, list by country)";

	private static final String SUCCESS_MESSAGE = "Deleting %s was successful!";

	private final CityDAO cityDAO;

	public DeleteCityOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner, false);
		this.cityDAO = CityDAO.getInstance();
	}

	@Override
	public boolean execute() {
		System.out.println(PROMPT_MESSAGE);
		try {
			City existingCity = promptCity();
			boolean success = cityDAO.deleteCity(existingCity);
			if (success) {
				PrintUtils.printSuccesOperation(String.format(SUCCESS_MESSAGE, existingCity.getName()));
			} else {
				System.out.println("Modifying city " + existingCity.getName() + " was not successfully because of unknown reason");
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


}

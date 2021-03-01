package com.ksteindl.worldjdbc.dao;

import com.ksteindl.worldjdbc.connectionpool.ConnectionPool;
import com.ksteindl.worldjdbc.connectionpool.ConnectionPoolFactory;
import com.ksteindl.worldjdbc.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountryDAO {

    private final ConnectionPool connectionPool;

    private static final String COUNTRIES_OF_CONTINENTS =
            "select country.Continent, country.Name AS 'countryName', city.Name AS 'capitalName' from country " +
                    "INNER JOIN city " +
                    "ON country.Capital = city.ID " +
                    "WHERE LOWER(country.Continent) LIKE ? " +
                    "ORDER BY country.Continent ASC, country.Name;";

    private static final String COUNTRIES_OF_LANGUAGE =
            "select country.Name, country.Population, lang.Language, lang.Percentage, lang.IsOfficial from country country " +
                    "INNER JOIN countrylanguage lang " +
                    "ON country.Code = lang.CountryCode " +
                    "WHERE LOWER(lang.Language) = ?" +
                    "ORDER BY IsOfficial ASC, (country.Population * lang.Percentage) DESC;";

    private static final String COUNTRIES_WITH_GNP = "select Name, Population, GNP, Continent from country;";

    private static final String COUNTRIES_BY_NAME = "select * from country where Name = ?;";

    private static CountryDAO INSTANCE;

    public static CountryDAO getInstance() {
        if (INSTANCE == null) {
            synchronized (CountryDAO.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CountryDAO();
                }
            }
        }
        return INSTANCE;
    }

    private CountryDAO() {
        this.connectionPool = ConnectionPoolFactory.getConnectionPool();
    }

    /* Assuming that a country has a unique name. If not, this method returns a random row with the given name, or null if no country is found.
    *
    * */
    public Country getCountry(String name) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(COUNTRIES_BY_NAME)) {
            statement.setString(1, name.toLowerCase());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Country country = new CountryBuilder()
                        .setName(resultSet.getString("Name"))
                        .setCode(resultSet.getString("Code"))
                        .setPopulation(resultSet.getLong("Population"))
                        .setContinent(resultSet.getString("Continent"))
                        .setGnp(resultSet.getDouble("GNP"))
                        .setCapital(new CityBuilder().setName(resultSet.getString("Capital")).createCity())
                        .createCountry();
                return country;
            }
            else {
                return null;
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public List<Country> getCountriesOfLanguage(String languageInput) throws SQLException {
        List<Country> countries = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(COUNTRIES_OF_LANGUAGE)) {
            statement.setString(1, languageInput.toLowerCase());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Language language = new LanguageBuilder()
                        .setLanguage(resultSet.getString("Language"))
                        .setIsOfficial(resultSet.getString("IsOfficial").equals("T"))
                        .setPercentage(resultSet.getDouble("Percentage"))
                        .createLanguage();
                Country country = new CountryBuilder()
                        .setName(resultSet.getString("Name"))
                        .setPopulation(resultSet.getLong("Population"))
                        .setLanguages(Arrays.asList(language))
                        .createCountry();
                countries.add(country);
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return countries;
    }

    public List<Country> getCountriesOfContinents(String contiQueryWord) throws SQLException {
        List<Country> countries = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(COUNTRIES_OF_CONTINENTS)) {
            statement.setString(1, contiQueryWord.toLowerCase());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Country country = new CountryBuilder()
                        .setName(resultSet.getString("countryName"))
                        .setContinent(resultSet.getString("Continent"))
                        .setCapital(new CityBuilder().setName(resultSet.getString("capitalName")).createCity())
                        .createCountry();
                countries.add(country);
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return countries;
    }

    public List<Country> getCountriesWithGnp() throws SQLException {
        List<Country> countries = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(COUNTRIES_WITH_GNP)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Country country = new CountryBuilder()
                        .setName(resultSet.getString("Name"))
                        .setContinent(resultSet.getString("Continent"))
                        .setGnp(resultSet.getDouble("GNP"))
                        .setPopulation(resultSet.getLong("Population"))
                        .createCountry();
                countries.add(country);
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return countries;
    }


}

package com.ksteindl.worldjdbc.dao;

import com.ksteindl.worldjdbc.connectionpool.ConnectionPool;
import com.ksteindl.worldjdbc.connectionpool.ConnectionPoolFactory;
import com.ksteindl.worldjdbc.model.City;
import com.ksteindl.worldjdbc.model.CityBuilder;
import com.ksteindl.worldjdbc.model.Country;
import com.ksteindl.worldjdbc.model.CountryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityDAO {

    private final ConnectionPool connectionPool;

    private static final String CITIES_IN_COUNTRY_QUERY =
            "select city.ID, city.Name, city.Population, city.District from city " +
                    "INNER JOIN country " +
                    "ON country.Code = city.CountryCode " +
                    "WHERE LOWER(country.Name) = ? " +
                    "ORDER BY city.Population DESC;";

    private static final String CITIES_LIKE =
            "select " +
                    "ID, " +
                    "city.ID AS 'cityId', " +
                    "city.Name AS 'cityName', " +
                    "country.Name AS 'countryName', " +
                    "city.Population AS 'cityPopulation', " +
                    "country.Population AS 'countryPopulation', " +
                    "country.Capital " +
            "from country " +
            "INNER JOIN city " +
            "ON city.CountryCode = country.Code " +
            "WHERE LOWER(city.Name) LIKE ? " +
            "ORDER BY city.Name ASC;";

    private static final String MAX_ID = "SELECT MAX(ID) AS maxId FROM city;";

    private static final String INSERT_CITY = "INSERT INTO city (ID, Name, CountryCode, District, Population) VALUES (?,?,?,?,?)";

    private static final String UPDATE_CITY = "UPDATE city " +
            "SET Name = ?, " +
            "CountryCode = ?, " +
            "District = ?, " +
            "Population = ? " +
            "WHERE ID = ?;";

    private static final String DELETE_CITY = "DELETE FROM city WHERE ID = ?;";

    private static final String CITY_BY_ID = "select * from city where ID = ?;";

    private CityDAO() {
        this.connectionPool = ConnectionPoolFactory.getConnectionPool();
    }

    private static CityDAO INSTANCE;

    public static CityDAO getInstance() {
        if (INSTANCE == null) {
            synchronized (CityDAO.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CityDAO();
                }
            }
        }
        return INSTANCE;
    }

    public List<City> getCitiesInCountry(String country) throws SQLException {
        List<City> cities = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(CITIES_IN_COUNTRY_QUERY)) {
            statement.setString(1, country.toLowerCase());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                City city = new CityBuilder()
                        .setID(resultSet.getLong("ID"))
                        .setName(resultSet.getString("Name"))
                        .setPopulation(resultSet.getLong("Population"))
                        .setDistrict(resultSet.getString("District"))
                        .createCity();
                cities.add(city);
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return cities;
    }

    public List<City> getCitiesLike(String queryWord) throws SQLException {
        List<City> cities = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(CITIES_LIKE)) {
            statement.setString(1, queryWord.toLowerCase());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Country country = new CountryBuilder()
                        .setName(resultSet.getString("countryName"))
                        .setCapital(new CityBuilder().setID(resultSet.getLong("Capital")).createCity())
                        .setPopulation(resultSet.getLong("countryPopulation"))
                        .createCountry();
                City city = new CityBuilder()
                        .setID(resultSet.getLong("cityId"))
                        .setName(resultSet.getString("cityName"))
                        .setPopulation(resultSet.getLong("cityPopulation"))
                        .setCountry(country)
                        .createCity();
                cities.add(city);
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return cities;
    }

    public boolean deleteCity(City city) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_CITY)) {
            statement.setLong(1, city.getID());
            statement.execute();
            return true;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public boolean modifyCity(City city) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_CITY)) {
            statement.setString(1, city.getName());
            statement.setString(2, city.getCountry().getCode());
            statement.setString(3, city.getDistrict());
            statement.setLong(4, city.getPopulation());
            statement.setLong(5, city.getID());
            statement.execute();
            return true;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public boolean insertCity(City city) throws SQLException {
        Long maxId = getMaxId();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_CITY)) {
            statement.setLong(1, maxId + 1);
            statement.setString(2, city.getName());
            statement.setString(3, city.getCountry().getCode());
            statement.setString(4, city.getDistrict());
            statement.setLong(5, city.getPopulation());
            statement.execute();
            return true;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public City getCity(Long ID) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(CITY_BY_ID)) {
            statement.setLong(1, ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                City city = new CityBuilder()
                        .setID(resultSet.getLong("ID"))
                        .setName(resultSet.getString("Name"))
                        .setDistrict(resultSet.getString("District"))
                        .setPopulation(resultSet.getLong("Population"))
                        .setCountry(new CountryBuilder().setCode(resultSet.getString("CountryCode")).createCountry())
                        .createCity();
                return city;
            }
            else {
                return null;
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private Long getMaxId() throws SQLException {
        Long maxId;
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(MAX_ID)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            maxId = resultSet.getLong("maxId");
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return maxId;
    }


}

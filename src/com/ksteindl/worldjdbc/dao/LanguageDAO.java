package com.ksteindl.worldjdbc.dao;

import com.ksteindl.worldjdbc.connectionpool.ConnectionPool;
import com.ksteindl.worldjdbc.connectionpool.ConnectionPoolFactory;
import com.ksteindl.worldjdbc.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageDAO {

    private static LanguageDAO INSTANCE;

    private final ConnectionPool connectionPool;

    private static final String LANGUAGES_OVER_TRESHOLD =
            "select SUM(lang.Percentage/100*country.Population) AS globalSpeakers, lang.Language from country country " +
                    "INNER JOIN countrylanguage lang " +
                    "ON country.Code = lang.CountryCode " +
                    "GROUP BY lang.Language " +
                    "HAVING SUM(lang.Percentage/100*country.Population) > ? " +
                    "ORDER BY SUM(lang.Percentage/100*country.Population) DESC;";

    private static final String LANGUAGE_COUNTRY_COUNT = "SELECT COUNT(Language) AS count FROM countrylanguage WHERE Language=?;";

    private static final String LANGUAGE_BY_NAME = "SELECT * FROM countrylanguage WHERE Language=?;";

    private static final String INSERT_LANGUAGE = "INSERT INTO countrylanguage (CountryCode, Language, IsOfficial, Percentage) VALUES (?,?,?,?)";

    private static final String UPDATE_LANGUAGE = "UPDATE countrylanguage " +
            "SET CountryCode = ?, " +
            "Language = ?, " +
            "IsOfficial = ?, " +
            "Percentage = ? " +
            "WHERE CountryCode = ?" +
            "AND Language = ?;";

    private static final String LANGUAGE_BY_COUNTRYCODE_AND_LANGUAGE = "SELECT * FROM countrylanguage WHERE CountryCode = ? AND Language = ?;";

    private static final String DELETE_LANGUAGE = "DELETE FROM countrylanguage WHERE CountryCode = ? AND Language = ?;";

    public static LanguageDAO getInstance() {
        if (INSTANCE == null) {
            synchronized (LanguageDAO.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LanguageDAO();
                }
            }
        }
        return INSTANCE;
    }

    private LanguageDAO() {
        this.connectionPool = ConnectionPoolFactory.getConnectionPool();
    }

    public List<LanguageGlobalStat> getLanguagesOverTreshold(Long treshold) throws SQLException {
        List<LanguageGlobalStatBuilder> languagesBuilder = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(LANGUAGES_OVER_TRESHOLD)) {
            statement.setLong(1, treshold);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LanguageGlobalStatBuilder languageBuilder = new LanguageGlobalStatBuilder()
                        .setName(resultSet.getString("Language"))
                        .setGlobalSpeakers(resultSet.getLong("globalSpeakers"));
                languagesBuilder.add(languageBuilder);
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return languagesBuilder.stream()
                .map(builder -> builder.setCountryCount(getLanguageCountryCount(builder.getName())))
                .map(builder -> builder.createLanguageGlobalStat())
                .collect(Collectors.toList());
    }

    public Integer getLanguageCountryCount(String language) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(LANGUAGE_COUNTRY_COUNT)) {
            statement.setString(1, language);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("count");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return -1;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public List<Language> getLanguagesByName(String languageName) throws SQLException {
        List<Language> languages = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(LANGUAGE_BY_NAME)) {
            statement.setString(1, languageName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Language language = new LanguageBuilder()
                        .setLanguage(resultSet.getString("Language"))
                        .setPercentage(resultSet.getDouble("Percentage"))
                        .setIsOfficial(resultSet.getString("IsOfficial").equals("T"))
                        .setCountryCode(resultSet.getString("CountryCode"))
                        .createLanguage();
                languages.add(language);
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return languages;
    }

    public Language getLanguagesByCountryCodeAndLanguage(String countryCode, String languageName) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(LANGUAGE_BY_COUNTRYCODE_AND_LANGUAGE)) {
            statement.setString(1, countryCode);
            statement.setString(2, languageName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Language language = new LanguageBuilder()
                        .setLanguage(resultSet.getString("Language"))
                        .setPercentage(resultSet.getDouble("Percentage"))
                        .setIsOfficial(resultSet.getString("IsOfficial").equals("T"))
                        .setCountryCode(resultSet.getString("CountryCode"))
                        .createLanguage();
                return language;
            } else {
                return null;
            }
        }finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public boolean insertLanguage(Language language) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_LANGUAGE)) {
            statement.setString(1, language.getCountryCode());
            statement.setString(2, language.getLanguage());
            statement.setString(3, language.getOfficial() ? "T" : "F");
            statement.setDouble(4, language.getPercentage());
            statement.execute();
            return true;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public boolean modifyLanguage(Language language) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_LANGUAGE)) {
            statement.setString(1, language.getCountryCode());
            statement.setString(2, language.getLanguage());
            statement.setString(3, language.getOfficial() ? "T" : "F");
            statement.setDouble(4, language.getPercentage());
            statement.setString(5, language.getCountryCode());
            statement.setString(6, language.getLanguage());
            statement.execute();
            return true;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public boolean deleteLanguage(Language language) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_LANGUAGE)) {
            statement.setString(1, language.getCountryCode());
            statement.setString(2, language.getLanguage());
            statement.execute();
            return true;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }


}

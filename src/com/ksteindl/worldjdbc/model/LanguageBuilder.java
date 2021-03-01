package com.ksteindl.worldjdbc.model;

public class LanguageBuilder {
    private String language;
    private Double percentage;
    private Boolean isOfficial;
    private String countryCode;

    public LanguageBuilder() {
    }

    public LanguageBuilder(Language language) {
        this.language = language.getLanguage();
        this.percentage = language.getPercentage();
        this.isOfficial = language.getOfficial();
        this.countryCode = language.getCountryCode();
    }



    public LanguageBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public LanguageBuilder setPercentage(Double percentage) {
        this.percentage = percentage;
        return this;
    }

    public LanguageBuilder setIsOfficial(Boolean isOfficial) {
        this.isOfficial = isOfficial;
        return this;
    }

    public LanguageBuilder setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public Language createLanguage() {
        return new Language(language, percentage, isOfficial, countryCode);
    }

    public String getLanguage() {
        return language;
    }

    public Double getPercentage() {
        return percentage;
    }

    public Boolean getOfficial() {
        return isOfficial;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
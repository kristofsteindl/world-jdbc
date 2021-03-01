package com.ksteindl.worldjdbc.model;

public class Language implements Printable{

	private String language;
	private Double percentage;
	private Boolean isOfficial;
	private String countryCode;


	public Language(String language, Double percentage, Boolean isOfficial, String countryCode) {
		this.language = language;
		this.percentage = percentage;
		this.isOfficial = isOfficial;
		this.countryCode = countryCode;
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

	@Override
	public String toString() {
		return "Language{" +
				", language='" + language + '\'' +
				", percentage=" + percentage +
				", isOfficial=" + isOfficial +
				'}';
	}
}

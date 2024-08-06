package com.example.country;

import java.util.Objects;

public class Country {

    private int countryCode;

    private String country;

    private String city;

    public Country(int countryCode, String country, String city) {
        this.countryCode = countryCode;
        this.country = country;
        this.city = city;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country1 = (Country) o;
        return Objects.equals(countryCode, country1.countryCode) && Objects.equals(country, country1.country) && Objects.equals(city, country1.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, country, city);
    }
}

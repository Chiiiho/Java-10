package com.example.country;

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
}

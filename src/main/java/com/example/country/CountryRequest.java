package com.example.country;

public class CountryRequest {

    private int countryCode;

    private String country;

    private String city;

    public CountryRequest(int countryCode, String country, String city) {
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
}

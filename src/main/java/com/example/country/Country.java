package com.example.country;

public class Country {

    private int id;
    private String country;
    private int countryCode;
    private String city;

    public Country(int id, String country, int countryCode, String city) {
        this.id = id;
        this.country = country;
        this.countryCode = countryCode;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public String getCity() {
        return city;
    }
}

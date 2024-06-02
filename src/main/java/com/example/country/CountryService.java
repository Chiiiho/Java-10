package com.example.country;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    private final CountryMapper countryMapper;

    public CountryService(CountryMapper countryMapper) {
        this.countryMapper = countryMapper;
    }

    public List<Country> findAll() {
        return countryMapper.findAll();
    }

    public List<Country> findByCountry(String prefix) {
        return countryMapper.findByCountryStartingWith(prefix);
    }

    public List<Country> findByCity(String prefix) {
        return countryMapper.findByCityStartingWith(prefix);
    }

    public Country findByCountryCode(int countryCode) {
        return countryMapper.findByCountryCode(countryCode)
                .orElseThrow(() -> new CountryNotFoundException("Country with code " + countryCode + " not found"));
    }

    public List<Country> getCountries(String countryStartsWith, String cityStartsWith) {
        if (!countryStartsWith.isEmpty()) {
            return findByCountry(countryStartsWith);
        } else if (!cityStartsWith.isEmpty()) {
            return findByCity(cityStartsWith);
        } else {
            return findAll();
        }
    }

    public Country insert(int countryCode, String country, String city) {
        Country country1 = new Country(countryCode, country, city);
        if (countryMapper.findByCountryCode(countryCode).isPresent()) {
            throw new CountryDuplicatedException("Country with code " + countryCode + " duplicated");
        }
        countryMapper.insert(country1);
        return country1;

    }

}

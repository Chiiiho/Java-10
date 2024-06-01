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

    public List<Country> findByCountryCode(int countryCode) {
        List<Country> countries = countryMapper.findByCountryCode(countryCode);
        if (countries.isEmpty()) {
            throw new CountryNotFoundException("Country with code " + countryCode + " not found");
        }
        return countries;
    }

    public Country findById(int id) {
        return countryMapper.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country with ID " + id + " not found"));
    }

        public List<Country> getCountries(String countryStartsWith, String cityStartsWith, Integer countryCode) {

        if (countryCode != null) {
            return findByCountryCode(countryCode);
        } else if (!countryStartsWith.isEmpty()) {
            return findByCountry(countryStartsWith);
        } else if (!cityStartsWith.isEmpty()) {
            return findByCity(cityStartsWith);
        } else {
            return findAll();
        }
    }

}

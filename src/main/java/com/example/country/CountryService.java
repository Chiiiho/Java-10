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

    public List<Country> findByCountryAndCity(String countryPrefix, String cityPrefix) {
        return countryMapper.findByCountryAndCityStartingWith(countryPrefix, cityPrefix);
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

}

package com.example.country;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/countries")
    public List<Country> getCountries(
            @RequestParam(name = "countryStartsWith", required = false, defaultValue = "") String countryStartsWith,
            @RequestParam(name = "cityStartsWith", required = false, defaultValue = "") String cityStartsWith,
            @RequestParam(name = "countryCode", required = false) Integer countryCode
            ) {
        if (countryCode != null) {
            return countryService.findByCountryCode(countryCode);
        } else if (!countryStartsWith.isEmpty() && !cityStartsWith.isEmpty()) {
            return countryService.findByCountryAndCity(countryStartsWith, cityStartsWith);
        } else if (!countryStartsWith.isEmpty()) {
            return countryService.findByCountry(countryStartsWith);
        } else if (!cityStartsWith.isEmpty()) {
            return countryService.findByCity(cityStartsWith);
        } else {
            return countryService.findAll();
        }
    }

    @GetMapping("/countries/{id}")
    public Country findById(@PathVariable("id") int id) {
        return countryService.findById(id);
    }

}

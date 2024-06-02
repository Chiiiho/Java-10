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
            @RequestParam(name = "cityStartsWith", required = false, defaultValue = "") String cityStartsWith
            ) {
        return countryService.getCountries(countryStartsWith, cityStartsWith);
    }

    @GetMapping("/countries/{country_code}")
    public Country findByCountryCode(@PathVariable("country_code") int countryCode) {
        return countryService.findByCountryCode(countryCode);
    }

}

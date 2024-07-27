package com.example.country;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    @PostMapping("/countries")
    public ResponseEntity<CountryResponse> insert(@RequestBody CountryRequest countryRequest, UriComponentsBuilder uriBuilder) {
        Country country = countryService.insert(countryRequest.getCountryCode(), countryRequest.getCountry(), countryRequest.getCity());
        URI location = uriBuilder.path("/countries/{country_code}").buildAndExpand(country.getCountryCode()).toUri();
        CountryResponse body = new CountryResponse("country created");
        return ResponseEntity.created(location).body(body);
    }

    @PatchMapping("/countries/{country_code}")
    public ResponseEntity<CountryResponse> update(@PathVariable("country_code") int countryCode, @RequestBody CountryRequest countryRequest) {
        Country country = countryService.update(countryCode, countryRequest.getCountry(), countryRequest.getCity());
        CountryResponse body = new CountryResponse("country updated");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/countries/{country_code}")
    public ResponseEntity<CountryResponse> delete(@PathVariable("country_code") int countryCode) {
        Country country = countryService.delete(countryCode);
        CountryResponse body = new CountryResponse("country deleted");
        return ResponseEntity.ok(body);
    }
}

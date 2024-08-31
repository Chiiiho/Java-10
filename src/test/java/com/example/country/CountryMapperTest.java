package com.example.country;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CountryMapperTest {

    @Autowired
    CountryMapper countryMapper;

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 全ての国が取得できること() {
        List<Country> countries = countryMapper.findAll();
        assertThat(countries)
                .hasSize(3)
                .contains(
                        new Country(36, "Hungary", "Budapest"),
                        new Country(43, "Austria", "Vienna"),
                        new Country(420, "The Czech Republic", "Prague")
                );
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 指定した国名の頭文字を含む国が取得できること() {
        List<Country> countries = countryMapper.findByCountryStartingWith("a");
        assertThat(countries)
                .hasSize(1)
                .contains(
                        new Country(43, "Austria", "Vienna")
                );
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 指定した国名の頭文字が存在しない場合取得されるリストが空であること() {
        List<Country> countries = countryMapper.findByCountryStartingWith("j");
        assertThat(countries)
                .isEmpty();
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 指定した都市名の頭文字を含む国が取得できること() {
        List<Country> countries = countryMapper.findByCityStartingWith("b");
        assertThat(countries)
                .hasSize(1)
                .contains(
                        new Country(36, "Hungary", "Budapest")
                );
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 指定した都市名の頭文字が存在しない場合取得されるリストが空であること() {
        List<Country> countries = countryMapper.findByCityStartingWith("a");
        assertThat(countries)
                .isEmpty();
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 指定した国番号が取得できること() {
        Optional<Country> countries = countryMapper.findByCountryCode(420);
        assertThat(countries)
                .isPresent()
                .hasValue(
                        new Country(420, "The Czech Republic", "Prague")
                );
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @ExpectedDataSet(value = "datasets/insert-countries.yml")
    @Transactional
    void 新たな国を登録できること() {
        Country country = new Country(385, "Croatia", "Zagreb");
        countryMapper.insert(country);

        Optional<Country> insertCountries = countryMapper.findByCountryCode(country.getCountryCode());
        assertThat(insertCountries)
                .isPresent();
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @ExpectedDataSet(value = "datasets/update-countries.yml")
    @Transactional
    void 国番号を指定して国名と都市名を更新できること() {
        Country existingCountry = new Country(36, "Republic of Hungary", "Szentendre");
        countryMapper.update(existingCountry);

        Optional<Country> updateCountries = countryMapper.findByCountryCode(36);
        assertThat(updateCountries)
                .isPresent()
                .hasValue(
                        new Country(36, "Republic of Hungary", "Szentendre")
                );
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @ExpectedDataSet(value = "datasets/delete-countries.yml")
    @Transactional
    void 国番号を指定して国を削除できること() {
        Country existingCountry = new Country(420, "The Czech Republic", "Prague");
        countryMapper.delete(existingCountry);

        Optional<Country> deleteCountries = countryMapper.findByCountryCode(420);
        assertThat(deleteCountries)
                .isEmpty();
    }

}

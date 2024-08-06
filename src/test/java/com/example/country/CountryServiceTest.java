package com.example.country;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @InjectMocks
    private CountryService countryService;

    @Mock
    private CountryMapper countryMapper;

    @Test
    public void 存在する国番号と国名と都市名を全て返す() {
        List<Country> countryList = List.of(
                new Country(31, "Netherlands", "Amsterdam"),
                new Country(33, "France", "Paris"),
                new Country(34, "Spain", "Madrid"),
                new Country(44, "the United Kingdom of Great Britain and Northern Ireland", "London"),
                new Country(49, "Germany", "Berlin"));
        doReturn(countryList).when(countryMapper).findAll();

        List<Country> actual = countryService.findAll();
        assertThat(actual).isEqualTo(countryList);

        verify(countryMapper, times(1)).findAll();
    }

    @Test
    public void 指定した国番号が存在する場合はその国番号と国名と都市名を返す() {
        doReturn(Optional.of(new Country(44, "the United Kingdom of Great Britain and Northern Ireland", "London"))).when(countryMapper).findByCountryCode(44);

        Country actual = countryService.findByCountryCode(44);
        assertThat(actual).isEqualTo(new Country(44, "the United Kingdom of Great Britain and Northern Ireland", "London"));

        verify(countryMapper, times(1)).findByCountryCode(44);
    }

    @Test
    public void 指定した国番号が存在しない場合は例外をスローする() {
        doReturn(Optional.empty()).when(countryMapper).findByCountryCode(50);

        assertThatThrownBy(() -> countryService.findByCountryCode(50)).isInstanceOf(CountryNotFoundException.class);

        verify(countryMapper, times(1)).findByCountryCode(50);
    }

    @Test
    public void 指定した国名の頭文字で存在する国番号と国名と都市名を返す() {
        doReturn(List.of(new Country(49, "Germany", "Berlin"))).when(countryMapper).findByCountryStartingWith("g");

        List<Country> actual = countryService.findByCountry("g");
        assertThat(actual).isEqualTo(List.of(new Country(49, "Germany", "Berlin")));

        verify(countryMapper, times(1)).findByCountryStartingWith("g");
    }

    @Test
    public void 指定した国名の頭文字で存在しない国名を検索し何も返さない() {
        doReturn(Collections.emptyList()).when(countryMapper).findByCountryStartingWith("k");

        List<Country> actual = countryService.findByCountry("k");
        assertThat(actual).isEmpty();

        verify(countryMapper, times(1)).findByCountryStartingWith("k");
    }

    @Test
    public void 指定した都市名の頭文字で存在する国番号と国名と都市名を返す() {
        doReturn(List.of(new Country(34, "Spain", "Madrid"))).when(countryMapper).findByCityStartingWith("m");

        List<Country> actual = countryService.findByCity("m");
        assertThat(actual).isEqualTo(List.of(new Country(34, "Spain", "Madrid")));

        verify(countryMapper, times(1)).findByCityStartingWith("m");
    }

    @Test
    public void 指定した都市名の頭文字で存在しない都市名を検索し何も返さない() {
        doReturn(Collections.emptyList()).when(countryMapper).findByCityStartingWith("y");

        List<Country> actual = countryService.findByCity("y");
        assertThat(actual).isEmpty();

        verify(countryMapper, times(1)).findByCityStartingWith("y");
    }

}

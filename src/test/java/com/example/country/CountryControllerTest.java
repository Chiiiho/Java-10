package com.example.country;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CountryService countryService;

    @Test
    void 全ての国を取得すること() throws Exception {
        List<Country> countryList = List.of(
                new Country(31, "Netherlands", "Amsterdam"),
                new Country(33, "France", "Paris"));
        when(countryService.getCountries("", "")).thenReturn(countryList);

        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        [
                            {
                                "countryCode":31,
                                "country":"Netherlands",
                                "city":"Amsterdam"
                            },
                            {
                                "countryCode":33,
                                "country":"France",
                                "city":"Paris"
                            }
                        ]
                        """
                ));

        verify(countryService, times(1)).getCountries("","");
    }

    @Test
    void 指定した国名と都市名の頭文字を含む国を取得すること() throws Exception {
        when(countryService.getCountries("n", "a")).thenReturn(List.of(new Country(31, "Netherlands", "Amsterdam")));

        mockMvc.perform(get("/countries")
                        .param("countryStartsWith", "n")
                        .param("cityStartsWith", "a"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        [
                            {
                                "countryCode":31,
                                "country":"Netherlands",
                                "city":"Amsterdam"
                            }
                        ]
                        """
                ));

        verify(countryService, times(1)).getCountries("n","a");
    }

    @Test
    void 指定した存在しない国名や都市名の頭文字で空の配列を返却すること() throws Exception {
        when(countryService.getCountries("k", "y")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/countries")
                        .param("countryStartsWith", "k")
                        .param("cityStartsWith", "y"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        []
                        """
                ));

        verify(countryService, times(1)).getCountries("k","y");
    }

    @Test
    void 指定した国番号を取得すること() throws Exception {
        when(countryService.findByCountryCode(31)).thenReturn(new Country(31, "Netherlands", "Amsterdam"));

        mockMvc.perform(get("/countries/{country_code}", 31))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "countryCode":31,
                            "country":"Netherlands",
                            "city":"Amsterdam"
                        }
                        """
                ));

        verify(countryService, times(1)).findByCountryCode(31);
    }

    @Test
    void 指定した国番号が存在しない場合は例外メッセージをスローすること() throws Exception {
        when(countryService.findByCountryCode(999)).thenThrow(new CountryNotFoundException("Country with code 999 not found"));

        mockMvc.perform(get("/countries/{country_code}", 999))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                        {
                            "message":"Country with code 999 not found"
                        }
                        """
                ));

        verify(countryService, times(1)).findByCountryCode(999);
    }

    @Test
    void 新たな国番号と国名と都市名を登録すること() throws Exception {
        when(countryService.insert(31, "Netherlands", "Amsterdam")).thenReturn(new Country(31, "Netherlands", "Amsterdam"));

        mockMvc.perform(post("/countries").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                    "countryCode":31,
                    "country":"Netherlands",
                    "city":"Amsterdam"
                }
                """
                ))
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        """
                        {
                            "message":"country created"
                        }
                        """
                ));

        verify(countryService, times(1)).insert(31, "Netherlands", "Amsterdam");
    }

    @Test
    void 登録しようとした国番号が既に存在する場合は例外メッセージをスローすること() throws Exception {
        Country existingCountry = new Country(31, "Netherlands", "Amsterdam");
        when(countryService.insert(31, "Netherlands", "Amsterdam")).thenThrow(new CountryDuplicatedException("Country with code 31 duplicated"));

        mockMvc.perform(post("/countries").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                    "countryCode":31,
                    "country":"Netherlands",
                    "city":"Amsterdam"
                }
                """
                ))
                .andExpect(status().isConflict())
                .andExpect(content().json(
                        """
                        {
                            "message":"Country with code 31 duplicated"
                        }
                        """
                ));

        verify(countryService, times(1)).insert(31, "Netherlands", "Amsterdam");
    }

    @Test
    void 国番号を指定して国名と都市名を更新すること() throws Exception {
        Country existingCountry = new Country(31, "Netherlands", "Amsterdam");
        when(countryService.update(31, "Netherlands", "Amsterdam")).thenReturn(existingCountry);

        mockMvc.perform(patch("/countries/{country_code}", 31).contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                    "countryCode":31,
                    "country":"Holland",
                    "city":"Rotterdam"
                }
                """
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "message":"country updated"
                        }
                        """
                ));

        verify(countryService, times(1)).update(31, "Holland", "Rotterdam");
    }

    @Test
    void 更新しようと指定した国番号が存在しない場合は例外をスローすること() throws Exception {
        Country existingCountry = new Country(31, "Netherlands", "Amsterdam");
        when(countryService.update(47, "Norway", "Oslo")).thenThrow(new CountryNotFoundException("Country with code 47 not found"));

        mockMvc.perform(patch("/countries/{country_code}", 47).contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                    "countryCode":47,
                    "country":"Norway",
                    "city":"Oslo"
                }
                """
                ))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                        {
                            "message":"Country with code 47 not found"
                        }
                        """
                ));

        verify(countryService, times(1)).update(47, "Norway", "Oslo");
    }

    @Test
    void 国番号を指定して国を削除すること() throws Exception {
        Country existingCountry = new Country(31, "Netherlands", "Amsterdam");
        when(countryService.delete(31)).thenReturn(existingCountry);

        mockMvc.perform(delete("/countries/{country_code}", 31))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "message":"country deleted"
                        }
                        """
                ));

        verify(countryService, times(1)).delete(31);
    }

    @Test
    void 削除しようと指定した国番号が存在しない場合は例外をスローすること() throws Exception {
        Country existingCountry = new Country(31, "Netherlands", "Amsterdam");
        when(countryService.delete(47)).thenThrow(new CountryNotFoundException("Country with code 47 not found"));

        mockMvc.perform(delete("/countries/{country_code}", 47))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                        {
                            "message":"Country with code 47 not found"
                        }
                        """
                ));

        verify(countryService, times(1)).delete(47);
    }
}

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
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    public void 全ての国を取得する() throws Exception {
        List<Country> countryList = List.of(
                new Country(31, "Netherlands", "Amsterdam"),
                new Country(33, "France", "Paris"));
        when(countryService.getCountries("", "")).thenReturn(countryList);

        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"countryCode\":31,\"country\":\"Netherlands\",\"city\":\"Amsterdam\"},{\"countryCode\":33,\"country\":\"France\",\"city\":\"Paris\"}]"));

        verify(countryService, times(1)).getCountries("","");
    }

    @Test
    public void 指定した国名と都市名の頭文字を含む国を取得する() throws Exception {
        when(countryService.getCountries("n", "a")).thenReturn(List.of(new Country(31, "Netherlands", "Amsterdam")));

        mockMvc.perform(get("/countries")
                        .param("countryStartsWith", "n")
                        .param("cityStartsWith", "a"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"countryCode\":31,\"country\":\"Netherlands\",\"city\":\"Amsterdam\"}]"));

        verify(countryService, times(1)).getCountries("n","a");
    }

    @Test
    public void 指定した存在しない国名や都市名の頭文字で何も返さない() throws Exception {
        when(countryService.getCountries("", "")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/countries")
                        .param("countryStartsWith", "")
                        .param("cityStartsWith", ""))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(countryService, times(1)).getCountries("","");
    }

    @Test
    public void 指定した国番号を取得する() throws Exception {
        when(countryService.findByCountryCode(31)).thenReturn(new Country(31, "Netherlands", "Amsterdam"));

        mockMvc.perform(get("/countries/{country_code}", 31))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"countryCode\":31,\"country\":\"Netherlands\",\"city\":\"Amsterdam\"}"));

        verify(countryService, times(1)).findByCountryCode(31);
    }

    @Test
    public void 指定した国番号が存在しない場合は例外メッセージをスローする() throws Exception {
        when(countryService.findByCountryCode(999)).thenThrow(new CountryNotFoundException("Country with code 999 not found"));

        mockMvc.perform(get("/countries/{country_code}", 999))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\": \"Country with code 999 not found\"}"));

        verify(countryService, times(1)).findByCountryCode(999);
    }

    @Test
    public void 新たな国番号と国名と都市名を登録する() throws Exception {
        when(countryService.insert(31, "Netherlands", "Amsterdam")).thenReturn(new Country(31, "Netherlands", "Amsterdam"));

        mockMvc.perform(post("/countries").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                   "countryCode": 31,
                   "country": "Netherlands",
                   "city": "Amsterdam"
                }
                """
                ))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"message\": \"country created\"}"));

        verify(countryService, times(1)).insert(31, "Netherlands", "Amsterdam");
    }

    @Test
    public void 登録しようとした国番号が既に存在する場合は例外メッセージをスローする() throws Exception {
        when(countryService.findByCountryCode(31)).thenThrow(new CountryDuplicatedException("Country with code 31 duplicated"));

        mockMvc.perform(get("/countries/{country_code}", 31))
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"message\": \"Country with code 31 duplicated\"}"));

        verify(countryService, times(1)).findByCountryCode(31);
    }

    @Test
    public void 国番号を指定して国名と都市名を更新する() throws Exception {
        Country existingCountry = new Country(31, "Netherlands", "Amsterdam");
        when(countryService.update(31, "Netherlands", "Amsterdam")).thenReturn(existingCountry);

        mockMvc.perform(patch("/countries/{country_code}", 31).contentType(MediaType.APPLICATION_JSON).content(
                        """
                        {
                           "countryCode": 31,
                           "country": "Holland",
                           "city": "Rotterdam"
                        }
                        """
                ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"country updated\"}"));

        verify(countryService, times(1)).update(31, "Holland", "Rotterdam");
    }

    @Test
    public void 国番号を指定して国を削除する() throws Exception {
        Country existingCountry = new Country(31, "Netherlands", "Amsterdam");
        when(countryService.delete(31)).thenReturn(existingCountry);

        mockMvc.perform(delete("/countries/{country_code}", 31))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"country deleted\"}"));

        verify(countryService, times(1)).delete(31);
    }
}

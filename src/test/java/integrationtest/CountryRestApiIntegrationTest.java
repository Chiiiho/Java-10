package integrationtest;

import com.example.country.CountryApplication;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CountryApplication.class)
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CountryRestApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 全ての国を取得すること() throws Exception {
        String response = mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals(
                """
                [
                    {
                        "countryCode":36,
                        "country":"Hungary",
                        "city":"Budapest"
                    },
                    {
                        "countryCode":43,
                        "country":"Austria",
                        "city":"Vienna"
                    },
                    {
                        "countryCode":420,
                        "country":"The Czech Republic",
                        "city":"Prague"
                    }
                ]
                """,
                response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 指定した国名と都市名の頭文字を含む国を取得すること() throws Exception {
        String response = mockMvc.perform(get("/countries")
                        .param("countryStartsWith", "h")
                        .param("cityStartsWith", "b"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals(
                """
                [
                    {
                        "countryCode":36,
                        "country":"Hungary",
                        "city":"Budapest"
                    }
                ]
                """,
                response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 指定した存在しない国名や都市名の頭文字で空の配列を返却すること() throws Exception {
        String response = mockMvc.perform(get("/countries")
                    .param("countryStartsWith", "c")
                    .param("cityStartsWith", "d"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals(
                """
                []
                """,
                response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 指定した国番号を取得すること() throws Exception {
        String response = mockMvc.perform(get("/countries/{country_code}", 36))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals(
                """
                {
                    "countryCode":36,
                    "country":"Hungary",
                    "city":"Budapest"
                }
                """,
                response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 指定した国番号が存在しない場合は例外メッセージをスローすること() throws Exception {
        mockMvc.perform(get("/countries/{country_code}", 999))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                        {
                            "message":"Country with code 999 not found"
                        }
                        """
                ));
}

    @Test
    @DataSet(value = "datasets/countries.yml")
    @ExpectedDataSet(value = "datasets/insert-countries.yml")
    @Transactional
    void 新たな国番号と国名と都市名を登録すること() throws Exception {
        mockMvc.perform(post("/countries").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                    "countryCode":385,
                    "country":"Croatia",
                    "city":"Zagreb"
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
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 登録しようとした国番号が既に存在する場合は例外メッセージをスローすること() throws Exception {
        mockMvc.perform(post("/countries").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                    "countryCode":36,
                    "country":"Hungary",
                    "city":"Budapest"
                }
                """
                ))
                .andExpect(status().isConflict())
                .andExpect(content().json(
                        """
                        {
                            "message":"Country with code 36 duplicated"
                        }
                        """
                ));
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @ExpectedDataSet(value = "datasets/update-countries.yml")
    @Transactional
    void 国番号を指定して国名と都市名を更新すること() throws Exception {
        mockMvc.perform(patch("/countries/{country_code}", 36).contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                    "countryCode":36,
                    "country":"Republic of Hungary",
                    "city":"Szentendre"
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
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 更新しようとした国番号が存在しない場合は例外メッセージをスローすること() throws Exception {
        mockMvc.perform(patch("/countries/{country_code}", 385).contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                    "countryCode":385,
                    "country":"Croatia",
                    "city":"Zagreb"
                }
                """
                ))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                        {
                            "message":"Country with code 385 not found"
                        }
                        """
                ));
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @ExpectedDataSet(value = "datasets/delete-countries.yml")
    @Transactional
    void 国番号を指定して国を削除すること() throws Exception {
        mockMvc.perform(delete("/countries/{country_code}", 420))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "message":"country deleted"
                        }
                        """
                ));
    }

    @Test
    @DataSet(value = "datasets/countries.yml")
    @Transactional
    void 削除しようとした国番号が存在しない場合は例外メッセージをスローすること() throws Exception {
        mockMvc.perform(delete("/countries/{country_code}", 385))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                        {
                            "message":"Country with code 385 not found"
                        }
                        """
                ));
    }

}

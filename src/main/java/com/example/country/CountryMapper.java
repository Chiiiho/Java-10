package com.example.country;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CountryMapper {

    @Select("SELECT * FROM countries")
    List<Country> findAll();

    @Select("SELECT * FROM countries WHERE country LIKE CONCAT(#{prefix}, '%')")
    List<Country> findByCountryStartingWith(String prefix);

    @Select("SELECT * FROM countries WHERE city LIKE CONCAT(#{prefix}, '%')")
    List<Country> findByCityStartingWith(String prefix);

    @Select("SELECT * FROM countries WHERE country_code = #{countryCode}")
    Optional<Country> findByCountryCode(int countryCode);

    @Insert("INSERT INTO countries (country_code, country, city) VALUES (#{countryCode}, #{country}, #{city})")
    void insert(Country country);

    @Update("UPDATE countries SET country = #{country}, city = #{city} WHERE country_code = #{countryCode}")
    void update(Country country);

}

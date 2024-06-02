package com.example.country;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    @Select("SELECT * FROM countries WHERE country_code = #{country_code}")
    Optional<Country> findByCountryCode(int countryCode);

}

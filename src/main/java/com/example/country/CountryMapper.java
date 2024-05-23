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

    @Select("SELECT * FROM countries WHERE country LIKE CONCAT(#{countryPrefix}, '%') OR city LIKE CONCAT(#{cityPrefix}, '%')")
    List<Country> findByCountryAndCityStartingWith(String countryPrefix, String cityPrefix);

    @Select("SELECT * FROM countries WHERE country_code = #{country_code}")
    List<Country> findByCountryCode(int countryCode);

    @Select("SELECT * FROM countries WHERE id = #{id}")
    Optional<Country> findById(int id);

}

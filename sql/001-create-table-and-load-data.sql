DROP TABLE IF EXISTS countries;

CREATE TABLE countries (
 country_code int NOT NULL,
 country VARCHAR(20) NOT NULL,
 city VARCHAR(20) NOT NULL,
 PRIMARY KEY(country_code)
);

INSERT INTO countries (country_code, country, city) VALUES (44, 'United Kingdom', 'London');
INSERT INTO countries (country_code, country, city) VALUES (33, 'France', 'Paris');
INSERT INTO countries (country_code, country, city) VALUES (49, 'Germany', 'Berlin');

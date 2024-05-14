DROP TABLE IF EXISTS countries;

CREATE TABLE countries (
 id int unsigned AUTO_INCREMENT,
 country VARCHAR(20) NOT NULL,
 country_code VARCHAR(20) NOT NULL,
 city VARCHAR(20) NOT NULL,
 PRIMARY KEY(id)
);

INSERT INTO countries (country, country_code, city) VALUES ('United Kingdom', '44', 'London');
INSERT INTO countries (country, country_code, city) VALUES ('France', '33', 'Paris');
INSERT INTO countries (country, country_code, city) VALUES ('Germany', '49', 'Berlin');

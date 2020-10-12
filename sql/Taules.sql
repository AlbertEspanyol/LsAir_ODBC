DROP DATABASE IF EXISTS  Local_lsAir;
CREATE DATABASE Local_lsAir;

USE Local_lsAir;

#taula plane
DROP TABLE IF EXISTS plane CASCADE;
CREATE TABLE plane (
  plane_id  INT NOT NULL,
  name      VARCHAR(255) DEFAULT NULL,
  iata_code text,
  icao_code text,
  PRIMARY KEY (plane_id)
);


#taula timezone
DROP TABLE IF EXISTS timezone CASCADE;
CREATE TABLE timezone (
  timezone_id          int(11)          NOT NULL,
  timezone_olson       VARCHAR(255)     DEFAULT NULL,
  timezone_utc         double           DEFAULT NULL,
  daylight_saving_time char(1)          DEFAULT NULL,
  PRIMARY KEY (timezone_id)
);

#taula country
DROP TABLE IF EXISTS country CASCADE;
CREATE TABLE country (
  country VARCHAR(255)  ,
  code    VARCHAR(2)    DEFAULT NULL,
  dst     VARCHAR(1)    DEFAULT NULL,
  PRIMARY KEY (country)
);

#taula airline
DROP TABLE IF EXISTS airline CASCADE;
CREATE TABLE airline (
  airline_id int(11)      NOT NULL,
  name       varchar(2)   DEFAULT NULL,
  alias      varchar(3)   DEFAULT NULL,
  iata       text,
  icao       text,
  country    varchar(255) DEFAULT NULL,
  PRIMARY KEY (airline_id),
  FOREIGN	KEY	(country)	REFERENCES	country (country)
);

#taula city
DROP TABLE IF EXISTS city CASCADE;
CREATE TABLE city (
  country varchar(255) NOT NULL,
  city    varchar(255) NOT NULL,
  PRIMARY KEY (city, country),
  FOREIGN	KEY	(country)	REFERENCES	country (country) ON DELETE CASCADE
);

#taula time zone city
DROP TABLE IF EXISTS  timezone_city CASCADE;
CREATE TABLE timezone_city (
  country     varchar(255) NOT NULL,
  city        varchar(255) NOT NULL,
  timezone_id int(11)      NOT NULL,
  PRIMARY KEY (country, city, timezone_id),
  KEY timezone_id (timezone_id),
  FOREIGN KEY (city, country) REFERENCES city (city, country) ON DELETE CASCADE,
  FOREIGN KEY (timezone_id) REFERENCES timezone (timezone_id) ON DELETE CASCADE
);


#taula airport
DROP TABLE IF EXISTS airport CASCADE;
CREATE TABLE airport (
  airport_id int(11)     NOT NULL,
  name       text,
  city       varchar(255) DEFAULT NULL,
  country    varchar(255) DEFAULT NULL,
  iata       text,
  icao       text,
  latitude   text,
  longitude  double       DEFAULT NULL,
  altitude   double       DEFAULT NULL,
  PRIMARY KEY (airport_id),
  FOREIGN	KEY	(city)	REFERENCES	city (city),
  FOREIGN	KEY	(country)	REFERENCES	country (country)
);

#taula route
DROP TABLE IF EXISTS route CASCADE;
CREATE TABLE route (
  route_id       INT(11) NOT NULL PRIMARY KEY ,
  airline_id     INT(11) DEFAULT NULL,
  src_airport_id INT(11) DEFAULT NULL,
  dst_airport_id INT(11) DEFAULT NULL,
  plane          INT(11) ,
  stops          text,
  codeshare      text,
  FOREIGN	KEY	(plane)	REFERENCES	plane (plane_id),
  FOREIGN	KEY	(airline_id)	REFERENCES	airline(airline_id),
  FOREIGN	KEY	(src_airport_id)	REFERENCES	airport(airport_id),
  FOREIGN	KEY	(dst_airport_id)	REFERENCES	airport(airport_id)
);

DROP TABLE IF EXISTS OLAP_airline CASCADE;
CREATE TABLE OLAP_airline (
  id_olap              int AUTO_INCREMENT PRIMARY KEY,
  -- timestamp    timestamp    ,
  airline_id   int(11)      NOT NULL,
  name         varchar(2)   DEFAULT NULL,
  alias        varchar(3)   DEFAULT NULL,
  iata         text,
  icao         text,
  country_name varchar(255) DEFAULT NULL,
  country_code VARCHAR(2)   DEFAULT NULL,
  country_dst  VARCHAR(1)   DEFAULT NULL
);

DROP TABLE IF EXISTS OLAP_airport CASCADE;
CREATE TABLE OLAP_airport (
  id_olap              int AUTO_INCREMENT PRIMARY KEY,
  -- timestamp           timestamp,
  airport_id           int(11) NOT NULL,
  name                 text,
  city                 varchar(255) DEFAULT NULL,
  country_name         varchar(255) DEFAULT NULL,
  country_code         VARCHAR(2)   DEFAULT NULL,
  country_dst          VARCHAR(1)   DEFAULT NULL,
  timezone_id          int(11),

  timezone_olson       VARCHAR(255) DEFAULT NULL,
  timezone_utc         double       DEFAULT NULL,
  daylight_saving_time char(1)      DEFAULT NULL,
  iata                 text,
  icao                 text,
  latitude             text,
  longitude            double       DEFAULT NULL,
  altitude             double       DEFAULT NULL
);

DROP TABLE IF EXISTS OLAP_route CASCADE;
CREATE TABLE OLAP_route (
  id_olap         int AUTO_INCREMENT PRIMARY KEY,
  -- timestamp       timestamp,
  route_id        INT(11) NOT NULL,
  airline_id      INT(11)      DEFAULT NULL,
  src_airport_id  INT(11)      DEFAULT NULL,
  dst_airport_id  INT(11)      DEFAULT NULL,
  stops           text,
  codeshare       text,
  plane_id        INT(11) NOT NULL,
  plane_name      VARCHAR(255) DEFAULT NULL,
  plane_iata_code text,
  plane_icao_code text,
  FOREIGN KEY (airline_id) REFERENCES OLAP_airline(id_olap),
  FOREIGN KEY (src_airport_id) REFERENCES OLAP_airport(id_olap),
  FOREIGN KEY (dst_airport_id) REFERENCES OLAP_airport(id_olap)
);


DROP TABLE IF EXISTS OLAP_time CASCADE;
CREATE TABLE OLAP_time (
  id_time int auto_increment primary key ,
  timestamp timestamp,
  src_airport_id INT(11) NOT NULL,
  dst_airport_id INT(11) NOT NULL,
  airline_id INT(11) NOT NULL,
  route_id INT(11) NOT NULL,
  FOREIGN KEY(src_airport_id) REFERENCES OLAP_airport(id_olap),
  FOREIGN KEY(dst_airport_id) REFERENCES OLAP_airport(id_olap),
  FOREIGN KEY (airline_id) REFERENCES OLAP_airline(id_olap),
  FOREIGN KEY (route_id) REFERENCES OLAP_route(id_olap)
);
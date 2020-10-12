USE Local_LsAir;

DELIMITER $$
DROP EVENT IF EXISTS update_week2 $$
CREATE EVENT IF NOT EXISTS update_week2
  ON SCHEDULE EVERY 2 WEEK STARTS CURDATE()
  ON COMPLETION NOT PRESERVE DO
  BEGIN

    INSERT INTO OLAP_airport(airport_id, name, city, country_name, iata, icao, latitude, longitude, altitude, country_code, country_dst, timezone_id, timezone_olson, timezone_utc, daylight_saving_time)
    SELECT a.airport_id, a.name, a.city, a.country, a.iata, a.icao, a.latitude, a.longitude, a.altitude, c.code, c.dst, t.timezone_id, t.timezone_olson, t.timezone_utc, t.daylight_saving_time
    FROM airport as a, country as c, timezone as t, timezone_city as tc WHERE a.country = c.country AND a.city = tc.city AND c.country =  tc.country  AND tc.timezone_id = t.timezone_id;

    INSERT INTO OLAP_airline(airline_id, name, alias, iata, icao, country_name, country_code, country_dst)
    SELECT a.airline_id, a.name, a.alias, a.iata, a.icao, a.country, c.code, c.dst FROM airline as a, country as c
    WHERE a.country = c.country;

    INSERT INTO OLAP_route( route_id, airline_id, src_airport_id, dst_airport_id, stops, codeshare, plane_id, plane_name, plane_iata_code, plane_icao_code)
    SELECT r.route_id, r.airline_id, r.src_airport_id, r.dst_airport_id, r.stops, r.codeshare, r.plane, p.name, p.iata_code, p.icao_code FROM route as r, plane as p
    WHERE r.plane = p.plane_id;

    INSERT INTO OLAP_time(timestamp, src_airport_id, dst_airport_id, airline_id, route_id)
    SELECT NOW(), a1.airport_id, al.airline_id, a2.airport_id, r.route_id FROM route AS r, airport AS a1, airline AS al, airport as a2
    WHERE r.airline_id = al.airline_id AND r.src_airport_id = a1.airport_id AND r.dst_airport_id = a2.airport_id;

  end; $$
DELIMITER ;
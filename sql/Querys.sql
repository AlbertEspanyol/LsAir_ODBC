USE Local_LSAir;

-- OLTP

#&&1
SELECT name FROM airport WHERE city LIKE 'Goroka';

#&&2
-- SELECCIONA el noms dels avions que han sortit de la timezone amb utc = 10 i totes les rutes que han fet.
SELECT DISTINCT p.name, COUNT(r.route_id) FROM plane p, route r, airport a, timezone_city tc, timezone t
WHERE p.plane_id = r.plane AND r.src_airport_id = a.airport_id AND a.country = tc.country AND tc.timezone_id = t.timezone_id
AND t.timezone_utc = 10 GROUP BY p.name;

#&&3
INSERT INTO route(route_id, airline_id, src_airport_id, dst_airport_id, plane, stops, codeshare) VALUES (1000000, 3,4,5,60,'hola', 'adeu');

#&&4
UPDATE route set src_airport_id = 6 WHERE route_id = 1000000;

#&&5
DELETE FROM route WHERE route_id = 1000000;

-- OLAP

#&&1
SELECT name FROM OLAP_airport WHERE city LIKE 'Goroka';

#&&2
-- SELECCIONA el noms dels avions que han sortit de la timezone amb utc = 10 i totes les rutes que han fet.
SELECT DISTINCT r.plane_name, COUNT(r.route_id) FROM OLAP_airport oa, OLAP_route r WHERE r.src_airport_id = oa.id_olap AND oa.timezone_utc = 10 GROUP BY r.plane_name;

#&&3
INSERT INTO OLAP_route(route_id, airline_id, src_airport_id, dst_airport_id, stops, codeshare, plane_id, plane_name, plane_iata_code, plane_icao_code)
VALUES (1000000, 3,4,5,'hola', 'adeu', 60, 'adeu', 'gog', 'vox');

#&&4
UPDATE OLAP_route set src_airport_id = 6 WHERE route_id = 1000000;

#&&5
DELETE FROM OLAP_route WHERE route_id = 1000000;

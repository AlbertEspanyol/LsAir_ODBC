import java.sql.PreparedStatement;
import java.sql.*;


public class DBManager {
    private Connector lsAir;
    private Connector local;

    public DBManager(Connector foreignServer, Connector localServer){
        lsAir = foreignServer;
        local = localServer;
    }

    public void copyDB(){
        PreparedStatement ps;
        try {

            local.deleteQuery("SET FOREIGN_KEY_CHECKS = 0");
            local.deleteQuery("TRUNCATE TABLE timezone");
            local.deleteQuery("TRUNCATE TABLE plane;");
            local.deleteQuery("TRUNCATE TABLE country;");
            local.deleteQuery("TRUNCATE TABLE airline;");
            local.deleteQuery("TRUNCATE TABLE city;");
            local.deleteQuery("TRUNCATE TABLE timezone_city;");
            local.deleteQuery("TRUNCATE TABLE airport;");
            local.deleteQuery("TRUNCATE TABLE route;");

            //local.deleteQuery("DELETE FROM timezone;");
            ResultSet rs = lsAir.selectQuery("SELECT * FROM timezone;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO timezone(timezone_id, timezone_olson, timezone_utc, daylight_saving_time) VALUES (?,?,?,?);");

                ps.setInt(1, rs.getInt("timezone_id"));
                ps.setString(2, rs.getString("timezone_olson"));
                ps.setDouble(3, rs.getDouble("timezone_utc"));
                ps.setString(4, rs.getString("daylight_saving_time"));

                ps.execute();
            }

            System.out.println("Tiemzone done");


            //local.deleteQuery("DELETE FROM plane;");
            rs = lsAir.selectQuery("SELECT * FROM plane;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO plane(plane_id, name, iata_code, icao_code) VALUES (?,?,?,?);");

                ps.setInt(1, rs.getInt("plane_id"));
                ps.setString(2, rs.getString("name"));
                ps.setString(3, rs.getString("iata_code"));
                ps.setString(4, rs.getString("icao_code"));

                ps.execute();
            }

            System.out.println("Plane done");


            //local.deleteQuery("DELETE FROM country;");
            rs = lsAir.selectQuery("SELECT * FROM country;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO country(country, code, dst) VALUES (?,?,?);");

                ps.setString(1, rs.getString("country"));
                ps.setString(2, rs.getString("code"));
                ps.setString(3, rs.getString("dst"));

                ps.execute();
            }

            System.out.println("Country done");


            //local.deleteQuery("DELETE FROM airline;");
            rs = lsAir.selectQuery("SELECT * FROM airline;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO airline(airline_id, name, alias, iata, icao, country) VALUES (?,?,?,?,?,?);");

                ps.setInt(1, rs.getInt("airline_id"));
                ps.setString(2, rs.getString("name"));
                ps.setString(3, rs.getString("alias"));
                ps.setString(4, rs.getString("iata"));
                ps.setString(5, rs.getString("icao"));
                ps.setString(6, rs.getString("country"));

                ps.execute();
            }

            System.out.println("Airline done");

            //local.deleteQuery("DELETE FROM city;");
            rs = lsAir.selectQuery("SELECT * FROM city;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO city(country, city) VALUES (?,?);");

                ps.setString(1, rs.getString("country"));
                ps.setString(2, rs.getString("city"));


                ps.execute();
            }

            System.out.println("City done");

            //local.deleteQuery("DELETE FROM timezone_city;");
            rs = lsAir.selectQuery("SELECT * FROM timezone_city;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO timezone_city(country, city, timezone_id) VALUES (?,?,?);");

                ps.setString(1, rs.getString("country"));
                ps.setString(2, rs.getString("city"));
                ps.setInt(3, rs.getInt("timezone_id"));

                ps.execute();
            }

            System.out.println("Timezone_City done");

            //local.deleteQuery("DELETE FROM airport;");
            rs = lsAir.selectQuery("SELECT * FROM airport;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO airport(airport_id, name, city, country, iata, icao, latitude, longitude, altitude) VALUES(?,?,?,?,?,?,?,?,?);");

                ps.setInt(1, rs.getInt("airport_id"));
                ps.setString(2, rs.getString("name"));
                ps.setString(3, rs.getString("city"));
                ps.setString(4, rs.getString("country"));
                ps.setString(5, rs.getString("iata"));
                ps.setString(6, rs.getString("icao"));
                ps.setString(7, rs.getString("latitude"));
                ps.setDouble(8, rs.getDouble("longitude"));
                ps.setDouble(9, rs.getDouble("altitude"));

                ps.execute();
            }

            System.out.println("Aiport done");

            //local.deleteQuery("DELETE FROM route;");
            rs = lsAir.selectQuery("SELECT * FROM route;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO route(route_id, airline_id, src_airport_id, dst_airport_id, codeshare, stops, plane) VALUES (?,?,?,?,?,?,?);");

                ps.setInt(1, rs.getInt("route_id"));
                ps.setInt(2, rs.getInt("airline_id"));
                ps.setInt(3, rs.getInt("src_airport_id"));
                ps.setInt(4, rs.getInt("dst_airport_id"));
                ps.setString(5, rs.getString("codeshare"));
                ps.setString(6, rs.getString("stops"));
                ps.setInt(7, rs.getInt("plane"));

                ps.execute();
            }

            System.out.println("Route done");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
    public void updateOLAP(){
        try{
            local.implementQuery("DELETE FROM OLAP_airport");
            local.implementQuery("DELETE FROM OLAP_route");
            local.implementQuery("DELETE FROM OLAP_airline");
            ResultSet rsLine = local.selectQuery("SELECT a.airline_id, a.name, a.alias, a.iata, a.icao, a.country, c.code, c.dst FROM airline as a, country as c WHERE a.country = c.country;");
            ResultSet rsAirport = local.selectQuery("SELECT a.airport_id, a.name, a.city, a.country, a.iata, a.icao, a.latitude, a.longitude, a.altitude, c.code, c.dst, t.timezone_id, t.timezone_olson, t.timezone_utc, t.daylight_saving_time " +
                    "FROM airport as a, country as c, timezone as t, timezone_city as tc WHERE a.country = c.country AND a.country = tc.country AND a.city = tc.city AND tc.timezone_id = t.timezone_id;");
            ResultSet rsRoute = local.selectQuery("SELECT r.route_id, r.airline_id, r.src_airport_id, r.dst_airport_id, r.plane, r.stops, r.codeshare, p.name, p.iata_code, p.icao_code FROM route as r, plane as p \n" +
                    "WHERE r.plane = p.plane_id;");

            while(rsLine.next()){

                local.implementQuery("INSERT INTO olap_airline(airport_id, name, city, country, iata, icao, latitude, longitude, altitude) " +
                        "VALUES(" + rs.getInt("airport_id") + ", '"
                        + rs.getString("name") + "', '" + rs.getString("city") + "', '"
                        + rs.getString("country") + "', '" + rs.getString("iata") + "', '"
                        + rs.getString("icao") + "', '" + rs.getString("latitude") + "', "
                        + rs.getDouble("longitude") + ", " + rs.getDouble("altitude") + ");");

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    */
    /*
    public void updateOLAP(){
        String insertSt = "VALUES(";
        String gordo = "INSERT INTO OLAP_lsAir(plane_id, timestamp, src_city_id, dst_city_id, src_country_id, dst_country_id, src_timezone_id, dst_timezone_id, src_timezone_olson, dst_timezone_olson, src_timezone_utc, dst_timezone_utc, src_daylight_saving_time, dst_daylight_saving_time, airline_id, airline_name, airline_alias, airline_iata, airline_icao, airline_country, route_id, route_codeshare, route_stops, plane_name, plane_iata, plane_icao, src_airport_id, src_airport_name, src_airport_iata, src_airport_icao, src_airport_latitude, src_airport_longitude, src_airport_altitude, dst_airport_name, dst_airport_iata, dst_airport_icao, dst_airport_latitude, dst_airport_longitude, dst_airport_altitude) ";
        try{
            local.implementQuery("DROP * FROM OLAP_lsAir;");
            ResultSet rsLine = local.selectQuery("SELECT * FROM airport;");
            ResultSet rsAirport = local.selectQuery("SELECT * FROM airport;");
            ResultSet rsCity = local.selectQuery("SELECT * FROM airport;");
            ResultSet rsCountry = local.selectQuery("SELECT * FROM airport;");
            ResultSet rsPlane = local.selectQuery("SELECT * FROM airport;");
            ResultSet rsRoute = local.selectQuery("SELECT * FROM airport;");
            ResultSet rsTimezone = local.selectQuery("SELECT * FROM airport;");
            ResultSet rsTimezoneCity = local.selectQuery("SELECT * FROM airport;");



            while (rsLine.next() || rsAirport.next() || rsCity.next() || rsCountry.next() || rsPlane.next() || rsRoute.next() || rsTimezone.next() || rsTimezone.next()){
                insertSt += rs.getInt("airport_id") + ", '"
                        + rs.getString("name") + "', '" + rs.getString("city") + "', '"
                        + rs.getString("country") + "', '" + rs.getString("iata") + "', '"
                        + rs.getString("icao") + "', '" + rs.getString("latitude") + "', "
                        + rs.getDouble("longitude") + ", " + rs.getDouble("altitude") + ");");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    */
}

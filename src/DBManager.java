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

            rs = lsAir.selectQuery("SELECT * FROM country;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO country(country, code, dst) VALUES (?,?,?);");

                ps.setString(1, rs.getString("country"));
                ps.setString(2, rs.getString("code"));
                ps.setString(3, rs.getString("dst"));

                ps.execute();
            }

            System.out.println("Country done");

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

            rs = lsAir.selectQuery("SELECT * FROM city;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO city(country, city) VALUES (?,?);");

                ps.setString(1, rs.getString("country"));
                ps.setString(2, rs.getString("city"));


                ps.execute();
            }

            System.out.println("City done");

            rs = lsAir.selectQuery("SELECT * FROM timezone_city;");

            while (rs.next()){
                ps = local.conn.prepareStatement("INSERT INTO timezone_city(country, city, timezone_id) VALUES (?,?,?);");

                ps.setString(1, rs.getString("country"));
                ps.setString(2, rs.getString("city"));
                ps.setInt(3, rs.getInt("timezone_id"));

                ps.execute();
            }

            System.out.println("Timezone_City done");

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
}

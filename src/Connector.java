import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;


public class Connector {
    private String userName;
    private String password;
    private String db;
    private int port;
    private String url = "jdbc:mysql://";
    public Connection conn = null;
    private Statement s;

    public Connector(String userName, String password, String db, String url, int port) {
        this.url += url;
        this.userName = userName;
        this.password = password;
        this.db = db;
        this.port = port;
        this.url += ":" + port + "/";
        this.url += db;
        this.url += "?verifyServerCertificate=false&useSSL=true";
    }
    
    public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Connection");
            conn = (Connection) DriverManager.getConnection(url, userName, password);
            if (conn != null) {
                System.out.println("Conexió a base de dades " + url + " ... Ok");
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("Problema al connecta-nos a la BBDD --> " + url + "  ");
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;

    }

    public void insertQuery(String query) {
        try {
            s = (Statement) conn.createStatement();
            s.executeUpdate(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Problema al Inserir --> " + ex.getSQLState());
        }
    }

    public void updateQuery(String query) {
        try {
            s = (Statement) conn.createStatement();
            s.executeUpdate(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Problema al Modificar --> " + ex.getSQLState());
        }
    }

    public void deleteQuery(String query) {
        try {
            s = (Statement) conn.createStatement();
            s.executeUpdate(query);

        } catch (SQLException ex) {
            System.out.println("Problema al Eliminar --> " + ex.getSQLState());
        }

    }

    public ResultSet selectQuery(String query) {
        ResultSet rs = null;
        try {
            s = (Statement) conn.createStatement();
            rs = s.executeQuery(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return rs;
    }

    public void disconnect() {
        try {
            conn.close();
            System.out.println("Desconnectat!");
        } catch (SQLException e) {
            System.out.println("Problema al tancar la connexió --> " + e.getSQLState());
        }
    }

}
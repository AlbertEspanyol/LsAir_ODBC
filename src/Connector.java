import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connector {
    private static String userName;
    private static String password;
    private static String database;
    private static String DBhost;
    private static String url;
    private static Connection conn;
    private static Statement s;
    private static Connector instance;

    private Connector(String user, String pass, String db, String host, int port){
        userName = user;
        password = pass;
        database = db;
        DBhost = host;
        url = "jdbc:mysql://" + host + ":"+port+"/"+db+"?verifyServerCertificate=false&useSSL=true";
        instance = null;
    }

    public static Connector getInstance(){
        if(instance == null){
            instance = new Connector(userName, password, database, DBhost, 3306);
            instance.connect();
        }
        return  instance;
    }

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Connection");
            conn = (Connection) DriverManager.getConnection(url, userName, password);
            if (conn != null) {
                System.out.println("Connexió a base de dades "+url+" ... Ok");
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            System.out.println("Problema al connecta-nos a la BBDD --> "+url);
        }
        catch(ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public void implementQuery(String query){
        try {
            s =(Statement) conn.createStatement();
            s.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Problema al Inserir --> " + ex.getSQLState());
        }
    }

    public ResultSet selectQuery(String query){
        ResultSet rs = null;
        try {
            s =(Statement) conn.createStatement();
            rs = s.executeQuery (query);
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return rs;
    }

    public void disconnect(){
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Problema al tancar la connexió --> " + e.getSQLState());
        }
    }
}


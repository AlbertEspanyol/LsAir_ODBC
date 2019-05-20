public class Main {
    public static void main(String[] args){

        Connector airConn = new Connector("lsair_user", "lsair_bbdd", Menu.showMenu(), "puigpedros.salle.url.edu", 3306);
        airConn.connect();

        Connector localConn = new Connector("root", "root", "Local_LsAir", "localhost", 3306);
        localConn.connect();

        DBManager dbm = new DBManager(airConn, localConn);

        dbm.copyDB();
    }
}


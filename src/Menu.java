import java.util.Scanner;

public class Menu {

    public static String showMenu(){
        String option;
        String db = "flight_db_00";
        boolean out = false;

        System.out.println("Benvingut a lsAir!\n");
        System.out.println("Tria una base de dades:\n\t - 1.flight_db_00 \n\t - 2.flight_db_01 \n\t - 3.flight_db_02");
        System.out.print("Opcio: ");

        Scanner sc = new Scanner(System.in);
        option = sc.nextLine();
        System.out.println();

        while (!out){
            switch (option){
                case "1":
                    db = "flight_db_00";
                    out = true;
                    break;
                case "2":
                    db = "flight_db_01";
                    out = true;
                    break;
                case "3":
                    db = "flight_db_02";
                    out = true;
                    break;
                default:
                    System.out.println("Perfavor, introdueixi una opcio correcta\n");
                    System.out.println("Tria una base de dades:\n\t - 1.flight_db_00 \n\t - 2.flight_db_01 \n\t - 3.flight_db_02");
                    System.out.print("Opcio: ");
                    option = sc.nextLine();
                    System.out.println();
            }
        }
        return db;
    }
}

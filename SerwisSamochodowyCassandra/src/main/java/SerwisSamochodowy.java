import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class SerwisSamochodowy {
    static Scanner scanner;
    static boolean koniec = false;

    static NaprawaDao dao;
    static CqlSession session;

    private static void addNew() {
        Naprawa naprawa = new Naprawa();
        System.out.println("Podaj id:");
        naprawa.id = Integer.parseInt(scanner.nextLine());
        System.out.println("Podaj imie właściciela:");
        naprawa.imie = scanner.nextLine();
        System.out.println("Podaj nazwisko właściciela:");
        naprawa.nazwisko = scanner.nextLine();
        System.out.println("Podaj date serwisu:");
        naprawa.data = scanner.nextLine();
        System.out.println("Podaj dane samochodu:");
        Samochod samochod = new Samochod();
        System.out.println("Podaj marke samochodu:");
        samochod.marka = scanner.nextLine();
        System.out.println("Podaj model samochodu:");
        samochod.model = scanner.nextLine();
        System.out.println("Podaj rok produkcji samochodu:");
        samochod.rok = Integer.parseInt(scanner.nextLine());
        System.out.println("Podaj moc samochodu:");
        samochod.moc = Integer.parseInt(scanner.nextLine());
        System.out.println("Podaj numer rejestracyjny samochodu:");
        samochod.rej = scanner.nextLine();
        naprawa.samo = samochod;
        System.out.println("Podaj liste usterek:");

        naprawa.usterki = new ArrayList<>();
        boolean koniecUst = false;
        while (!koniecUst) {
            String text = scanner.nextLine();
            if (text.equals("0")==false) {
                naprawa.usterki.add(text);
            } else {
                koniecUst = true;
            }
        }

        dao.save(naprawa);
    }

    private static void findById() {
        System.out.println("Podaj id:");
        int id = Integer.parseInt(scanner.nextLine());
        Naprawa naprawa = dao.findById(id);
        System.out.println("Wizyta{" +
                "id=" + naprawa.id +
                ", imie='" + naprawa.imie + '\'' +
                ", nazwisko='" + naprawa.nazwisko + '\'' +
                ", data='" + naprawa.data + '\'' +
                ", Samochod{" +
                "marka='" + naprawa.samo.marka + '\'' +
                ", model='" + naprawa.samo.model + '\'' +
                ", rok=" + naprawa.samo.rok +
                ", moc=" + naprawa.samo.moc +
                ", rej='" + naprawa.samo.rej + '\'' +
                ", usterki=" + naprawa.usterki +
                '}');
    }

    private static void update(){
        System.out.println("Podaj id:");
        int id = Integer.parseInt(scanner.nextLine());
        Naprawa naprawa = dao.findById(id);
        System.out.println("Wizyta{" +
                "id=" + naprawa.id +
                ", imie='" + naprawa.imie + '\'' +
                ", nazwisko='" + naprawa.nazwisko + '\'' +
                ", data='" + naprawa.data + '\'' +
                ", Samochod{" +
                "marka='" + naprawa.samo.marka + '\'' +
                ", model='" + naprawa.samo.model + '\'' +
                ", rok=" + naprawa.samo.rok +
                ", moc=" + naprawa.samo.moc +
                ", rej='" + naprawa.samo.rej + '\'' +
                ", usterki=" + naprawa.usterki +
                '}');

        System.out.println("Podaj id:");
        naprawa.id = Integer.parseInt(scanner.nextLine());
        System.out.println("Podaj imie właściciela:");
        naprawa.imie = scanner.nextLine();
        System.out.println("Podaj nazwisko właściciela:");
        naprawa.nazwisko = scanner.nextLine();
        System.out.println("Podaj date serwisu:");
        naprawa.data = scanner.nextLine();
        System.out.println("Podaj dane samochodu:");
        Samochod samochod = new Samochod();
        System.out.println("Podaj marke samochodu:");
        samochod.marka = scanner.nextLine();
        System.out.println("Podaj model samochodu:");
        samochod.model = scanner.nextLine();
        System.out.println("Podaj rok produkcji samochodu:");
        samochod.rok = Integer.parseInt(scanner.nextLine());
        System.out.println("Podaj moc samochodu:");
        samochod.moc = Integer.parseInt(scanner.nextLine());
        System.out.println("Podaj numer rejestracyjny samochodu:");
        samochod.rej = scanner.nextLine();
        naprawa.samo = samochod;
        System.out.println("Podaj liste usterek:");

        boolean koniecUst = false;
        while (!koniecUst) {
            String text = scanner.nextLine();
            if (text.equals("0")==false) {
                naprawa.usterki.add(text);
            } else {
                koniecUst = true;
            }
        }

        dao.update(naprawa);
    }

    static private  void delete(){
        System.out.println("Podaj id:");
        int id = Integer.parseInt(scanner.nextLine());
        Naprawa naprawa = dao.findById(id);
        dao.delete(naprawa);
    }

    static private void showMenu() {
        System.out.println("\n**************  MENU:  ***************");
        System.out.println("1 - Dodaj  ");
        System.out.println("2 - Wyświetl  po  id ");
        System.out.println("3 - Zaktualizuj ");
        System.out.println("4 - Usuń po id");
        System.out.println("0 - WYJŚCIE\n");
    }

    static private void getWyborMenu() {
        int choice = -1;
        do {
            System.out.println("Podaj wybór:");
            choice = Integer.parseInt(scanner.nextLine());
            if (choice < 0 || choice > 6) {
                System.out.println("Brak takiej opcji!");
            }
        } while (choice < 0 || choice > 6);
        akcja(choice);
    }

    static private void akcja(int wybor) {
        switch (wybor) {
            case 1:
                addNew();
                break;
            case 2:
                findById();
                break;
            case 3:
                update();
                break;
            case 4:
                delete();
                break;
            case 0:
                koniec = true;
                break;
            default:
                System.out.println("BŁĄD");
        }
    }

    static void menu() {
        while (!koniec) {
            showMenu();
            getWyborMenu();
        }
    }


    public static void main(String[] args) throws UnknownHostException {
        scanner = new Scanner(System.in);

        Builder builder = new Builder();
        session = builder.init();

        SerwisTableManager serwisTableManager = new SerwisTableManager(session);
        serwisTableManager.createTable();

        CodecManager codecManager = new CodecManager(session);
        codecManager.registerAddressCodec();

        NaprawaMapper naprawaMapper = new NaprawaMapperBuilder(session).build();
        dao = naprawaMapper.naprawaDao(CqlIdentifier.fromCql("serwis"));

        menu();
        scanner.nextLine();
    }
}

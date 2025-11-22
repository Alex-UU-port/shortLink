import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<User> users = new ArrayList<>();
    LinkManager manager = new LinkManager();

    public static void main(String[] args) {

        users = User.fromJSON();

        //меню авторизации, регистрации и просмотра пользователей с UUID
        label:
        while(true) {
            System.out.println("Введите \"1\", для авторизации;\n" +
                    "Введите \"2\", для регистрации нового пользователя;\n" +
                    "Введите \"3\", для просмотра ранее зарегистрированных пользователей;\n" +
                    "Введите \"0\", для выхода из программы;\n");
            String cmd = scanner.nextLine();
            switch (cmd) {
                case "1":
                    toAuthorization();
                    toRun();
                    break;
                case "2":
                    toRegister();
                    break;
                case "3":
                    toPrintUsers(users);
                    break;
                case "0":
                    User.saveJSON(users);
                    break label;
                default:
                    System.out.println("Неизвестная команда!");
                    break;
            }
        }

    }

    //меню управления ссылками
    public static void toRun() {
        label:
        while(true) {
            System.out.println("Введите \"1\", для просмотра ранее созданных коротких ссылок;\n" +
                            "Введите \"2\", для создания новой короткой ссылки;\n" +
                            "Введите \"3\" для удаления короткой ссылки;\n" +
                    "Введите \"0\", для выхода из программы;\n");
            String cmd = scanner.nextLine();
            switch (cmd) {
                case "1":

                    break;
                case "2":

                    break;
                case "3":
                    //String shortUrl = makeShortLink();

                    break;
                case "0":
                    break label;
                default:
                    System.out.println("Неизвестная команда!");
                    break;
            }
        }
    }

    public static void toRegister() {
        System.out.println("\nВведите логин: ");
        String log = scanner.nextLine();

        System.out.println("\nВведите пароль: ");
        String pass = scanner.nextLine();

        //проверка на наличие пользователя с таким же именем
        boolean isBe = false;
        for (User user : users) {
            if (log.equals(user.getLogin())) {
                System.out.println("\nПользователь с таким именем уже существует!\n");
                isBe = true;
                break;
            }
        }
        if (!isBe) {
            User user = new User(log, pass);
            users.add(user);
        }
    }

    public static void toPrintUsers(List<User> users) {
        if (!users.isEmpty()) {
            for (User user : users) {
                user.toPrint();
            }
        } else {
            System.out.println("Зарегистрированных пользователей не найдено!");
        }
        System.out.println("\n");
    }

    public static User toAuthorization() {
        System.out.println("\nВведите логин: ");
        String log = scanner.nextLine();

        System.out.println("\nВведите пароль: ");
        String pass = scanner.nextLine();

        User activUser = null;

        for (User user : users) {
            if (log.equals(user.getLogin()) && pass.equals(user.getPassword())) {
                System.out.println("\nПользователь найден!\n");
                activUser = user;
                break;
            }
        }
        return activUser;
    }

                /*
            System.out.println("Введите длинный URL для сокращения:");
            String longUrl = scanner.nextLine();


            // Создаем короткую ссылку
            String shortUrl = manager.createShortLink(longUrl, userUuid, 5, 24); // лимит 5 и срок 24 часа
            System.out.println("Создана короткая ссылка: " + shortUrl);

            // имитируем переход
            System.out.println("Переход по короткой ссылке...");
            String original = manager.getOriginalUrl(shortUrl.replace("clck.ru/", ""));
            if (original != null) {
                System.out.println("Редирект на: " + original);
                Desktop.getDesktop().browse(new URI(original));
            } else {
                System.out.println("Ссылка недоступна или лимит исчерпан.");
            }
            scanner.close();
            */

}
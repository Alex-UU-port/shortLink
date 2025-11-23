import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static private List<User> users = new ArrayList<>();
    static LinkManager manager = new LinkManager();

    public static void main(String[] args) {

        users = User.fromJSON("users.json");

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
                    User activeUser = toAuthorization();
                    if(activeUser != null) {
                    toRun(activeUser);}
                    break;
                case "2":
                    toRegister();
                    break;
                case "3":
                    toPrintUsers(users);
                    break;
                case "0":
                    User.saveJSON(users, "users.json");
                    manager.saveJSON(manager.links, "links.json");
                    break label;
                default:
                    System.out.println("Неизвестная команда!");
                    break;
            }
        }
        scanner.close();
    }

    //меню управления ссылками
    public static void toRun(User activeUser) {
        label:
        while(true) {
            System.out.println(activeUser.getLogin() + ": \n" +
                            "Введите \"1\", для просмотра ранее созданных коротких ссылок;\n" +
                            "Введите \"2\", для создания новой короткой ссылки;\n" +
                            "Введите \"3\" для перехода по короткой ссылке;\n" +
                            "Введите \"0\", для выхода из программы;\n");
            String cmd = scanner.nextLine();
            switch (cmd) {
                case "1":
                    manager.toPrintLinksKey();
                    break;
                case "2":
                    toCreateShortLink(activeUser);
                    break;
                case "3":
                    toRedirect();
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
            } else {
                System.out.println("\nПользователя с таким именем и паролем нет!\n");
            }
        }
        return activUser;
    }

    public static void toCreateShortLink(User activeUser) {
        System.out.println("Введите длинный URL для сокращения:");
        String longUrl = scanner.nextLine();

        // Создаем короткую ссылку
        String shortUrl = manager.createShortLink(longUrl, activeUser, 5, 24); // лимит 5 и срок 24 часа
        System.out.println("Создана короткая ссылка: " + shortUrl);
    }

    public static void toRedirect() {
        System.out.println("\nВведите короткую ссылку: ");
        String strShortLink = scanner.nextLine();
        String shortUrl = strShortLink.replace("clck.ru/", "");
        System.out.println(shortUrl);


        if (manager.getLinks().containsKey(shortUrl)) {
            System.out.println("Переход по короткой ссылке...");
            String original = manager.getOriginalUrl(shortUrl);

            System.out.println("Редирект на: " + original);
            try {
                Desktop.getDesktop().browse(new URI(original));
            } catch (URISyntaxException | IOException e) {
                System.out.println("Что то пошло не так" + e.getMessage());
            }
        } else {
            System.out.println("Ссылка не найдена!");
        }
    }
}
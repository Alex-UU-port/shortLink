import java.awt.Desktop;
import java.net.URI;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    /*
    Для реального сервиса нужно добавить возможность хранить данные в базе данных.
    Реализовать автоматическую очистку устаревших ссылок (через задачу, Cron или планировщик).
    Сделать REST API (например, Spring Boot) для более удобного доступа.
    Обеспечить безопасность доступа к ссылкам и управление ними.
    Добавить обработку ошибок, логирование, уведомления.
    */
    //sdsdsd

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        LinkManager manager = new LinkManager();

        UUID userUuid = UUID.randomUUID(); // при первом использовании
        System.out.println("Ваш UUID: " + userUuid);

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
    }
}
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class LinkManager {
    Map<String, ShortLink> links = new HashMap<>();

    private Random random = new Random();

    // Создание короткой ссылки
    public String createShortLink(String originalUrl, User user, int maxRedirects, int lifespanHours) {
        String shortCode = generateUniqueShortCode();

        ShortLink link = new ShortLink(originalUrl, shortCode, user, maxRedirects, lifespanHours);
        links.put(shortCode, link);
        return "clck.ru/" + shortCode;
    }

    // Генерация уникального короткого кода
    private String generateUniqueShortCode() {
        String code;
        do {
            code = generateRandomCode(6); // длина 6 символов
        } while (links.containsKey(code));
        return code;
    }

    private String generateRandomCode(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // Получение ссылки по коду
    public String getOriginalUrl(String shortCode) {
        ShortLink link = links.get(shortCode);
        if (link == null) {
            return null;
        }
        if (link.isExpired() || link.getCurrentRedirects() >= link.getMaxRedirects()) {
            links.remove(shortCode);
            return null;
        }
        if (link.canRedirect()) {
            link.incrementRedirects();
            return link.getOriginalUrl();
        }
        return null;
    }

    public void toPrintLinksKey() {
        if (!links.isEmpty()) {
            for (String key : links.keySet()) {
                System.out.println("clck.ru/" + key);
            }
        } else {
            System.out.println("Ссылок не найдено не найдено!");
        }
        System.out.println("\n");
    }

    public static void saveJSON (Map<String, ShortLink> links, String path) {

        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        // Используем try-with-resources, чтобы автоматически закрывать writer

        try {
            objectMapper.writeValue(new File(path), links);
            System.out.println("Данные успешно сохранены в файл links.json");
        } catch (IOException e) {
            System.out.println("Какие-то проблемы с формированием json: " + e.getMessage());
        }
    }

    public static List<User> fromJSON(String path) {
        //List<User> users = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {

                ShortLink shortLink = mapper.readValue(line, User.class);
                links.put(user);
            }
        } catch (IOException e) {
            System.out.println("Какие-то проблемы с чтением json: " + e.getMessage());
        }
        return users;
    }

    // Дополнительные методы: удаление, получение по UUID пользователя, и др.
}

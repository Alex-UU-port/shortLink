import java.util.*;

public class LinkManager {
    private Map<String, ShortLink> links = new HashMap<>();
    private Random random = new Random();

    // Создание короткой ссылки
    public String createShortLink(String originalUrl, UUID userId, int maxRedirects, int lifespanHours) {
        String shortCode = generateUniqueShortCode();

        ShortLink link = new ShortLink(originalUrl, shortCode, userId, maxRedirects, lifespanHours);
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

    // Дополнительные методы: удаление, получение по UUID пользователя, и др.
}

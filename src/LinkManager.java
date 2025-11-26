import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class LinkManager {
    Map<String, ShortLink> links = new HashMap<>();
    private Random random = new Random();

    public Map<String, ShortLink> getLinks() {
        return this.links;
    }

    public LinkManager() {
        fromJSON("links.json");
    }

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
        } else if (link.canRedirect()) {
            link.incrementRedirects();
            return link.getOriginalUrl();
        }
        return null;
    }

    public void toPrintLinksKey() {
        if (!links.isEmpty()) {
            for (String key : links.keySet()) {
                //System.out.println("clck.ru/" + key);
                ShortLink shortLink = links.get(key);
                shortLink.toPrint();
            }
        } else {
            System.out.println("Ссылок не найдено не найдено!");
        }
        System.out.println("\n");
    }

    public void saveJSON (Map<String, ShortLink> links, String path) {

        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        // Используем try-with-resources, чтобы автоматически закрывать writer
        try {
            objectMapper.writeValue(new File(path), links);
            System.out.println("Данные успешно сохранены в файл links.json");
        } catch (IOException e) {
            System.out.println("Какие-то проблемы с формированием json: " + e.getMessage());
        }
    }

    public void fromJSON(String path) {

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        try {
            this.links = mapper.readValue(new File(path), new TypeReference<Map<String, ShortLink>>() {});
        } catch (IOException e) {
            System.out.println("Какие-то проблемы с чтением json: " + e.getMessage());
        }
        //return links;
    }

    public boolean toRightsCheck(String shortLink, User activeUser) {
        if (links.containsKey(shortLink)) {
            ShortLink link = links.get(shortLink);
            if (activeUser.equals(link.getUser())) {
                return true;
            } else {
                System.out.println("Ссылка принадлежит пользователю " + link.getUser().getLogin() +
                ", вы не можете её удалить или редактировать");
                return false;
            }
        } else {
            System.out.println("Ссылка не найдена!");
            return false;
        }
    }

    public void toRemove(String shortLink, User activeUser) {
        if(toRightsCheck(shortLink, activeUser)) {
            links.remove(shortLink);
            System.out.println("Ссылка удалена!");
        }
    }

    public int toReadMaxRedirects() {
        int maxRedirects = 0;

        while (maxRedirects < 1) {
            System.out.println("\nВведите новое максимальное количество переходов по ссылке (от 1 до  2 147 483 647): ");
            String line = Main.scanner.nextLine();
            try {
                maxRedirects = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Введенное значение не соответствует требованиям!\n");
            }
        }
        return maxRedirects;
    }

    public void toEditRedirect(String shortLink, User activeUser) {
        if(toRightsCheck(shortLink, activeUser)) {
            int maxRedirects = toReadMaxRedirects();
            links.get(shortLink).setMaxRedirects(maxRedirects);
            System.out.println("\nМаксимальное количество переходов изменено!");
        }
    }

    public LocalDateTime toReadExpiryTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime dateTime;

        while (true) {
        System.out.println("\nВведите новую дату и время до которой ссылка будет доступна в формате \"dd-MM-yyyy HH:mm:ss\" : ");
        String line = Main.scanner.nextLine();
        try {
            dateTime = LocalDateTime.parse(line, formatter);
            if (dateTime.isAfter(LocalDateTime.now())) {
                break;
            } else {
                System.out.println("\nДата и время должны быть позднее настоящего времени!");
            }

        } catch (DateTimeParseException e) {
            System.out.println("\nНекорректный формат даты и времени!");
        }
        }
        return dateTime;
    }

    public void toEditExpiryTime(String shortLink, User activeUser) {
        if(toRightsCheck(shortLink, activeUser)) {
            LocalDateTime dateTime = toReadExpiryTime();
            links.get(shortLink).setExpiryTime(dateTime);
            System.out.println("\nВремя существования ссылки (доступности), изменено!");
        }
    }

}
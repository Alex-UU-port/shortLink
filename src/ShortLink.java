import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShortLink {
    private String originalUrl;
    private String shortCode;
    private User user;
    private int maxRedirects;
    private int currentRedirects;
    private LocalDateTime creationTime;
    private LocalDateTime expiryTime;

    public ShortLink(String originalUrl, String shortCode, User user, int maxRedirects, int lifespanHours) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.user = user;
        this.maxRedirects = maxRedirects;
        this.currentRedirects = 0;
        this.creationTime = LocalDateTime.now();
        this.expiryTime = creationTime.plusHours(lifespanHours);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    public boolean canRedirect() {
        return !isExpired() && (currentRedirects < maxRedirects);
    }

    public void incrementRedirects() {
        currentRedirects++;
    }

    public int getCurrentRedirects() {
        return this.currentRedirects;
    }

    public String getShortCode() {
        return this.shortCode;
    }

    public int getMaxRedirects() {
        return this.maxRedirects;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String line = "Оригинальный URL: " + this.originalUrl + "\tКороткая ссылка: " + this.shortCode + "Пользователь: " + this.user.getLogin() +
                        "Максимальное разрешенное количество переходов: " +  this.maxRedirects + "Совершенных переходов: " + this.currentRedirects +
                        "Дата создания: " + this.creationTime.format(formatter) + "Продолжительность существования до: " + this.expiryTime.format(formatter);
        return line;
    }

    public void toPrint() {
        System.out.println(this.toString());
    }

}

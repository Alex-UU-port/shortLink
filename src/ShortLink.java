import java.time.LocalDateTime;
import java.util.UUID;

public class ShortLink {
    private String originalUrl;
    private String shortCode;
    private UUID userId;
    private int maxRedirects;
    private int currentRedirects;
    private LocalDateTime creationTime;
    private LocalDateTime expiryTime;

    public ShortLink(String originalUrl, String shortCode, UUID userId, int maxRedirects, int lifespanHours) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.userId = userId;
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

    public int getMaxRedirects() {
        return this.maxRedirects;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }
}

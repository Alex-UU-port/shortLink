import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String login;
    private String password;
    private UUID userId;


    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.userId = UUID.randomUUID();
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public UUID getUserId() {
        return this.userId;
    }

    @Override
    public String toString() {
        String line = "Логин: " + this.login + "\tUUID: " + this.userId;
        return line;
    }

    public void toPrint() {
        System.out.println(this.toString());
    }

    public static void saveJSON (List<User> users, String path) {

        // Используем try-with-resources, чтобы автоматически закрывать writer
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
            for (User user : users) {
                String jsonString = objectMapper.writeValueAsString(user);

                writer.write(jsonString);
                writer.newLine();
            }
            System.out.println("Данные успешно сохранены в файл users.json");
        } catch (IOException e) {
            System.out.println("Какие-то проблемы с формированием json: " + e.getMessage());
        }
    }

    public static List<User> fromJSON(String path) {
        List<User> users = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {

                User user = mapper.readValue(line, User.class);
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Какие-то проблемы с чтением json: " + e.getMessage());
        }
        return users;
    }

}


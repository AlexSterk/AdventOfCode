package setup;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Download {
    public static void main(String[] args) throws IOException, InterruptedException {
        String sessionToken = Files.readString(Paths.get("data/session.token")).trim();
        String year = Files.readString(Paths.get("data/year.txt")).trim();
        int day = getDay();
        downloadInput(sessionToken, year, day);
        createTemplate(day);
    }

    private static void createTemplate(int day) throws IOException {
        Path classFilePath = Paths.get(String.format("src/days/Day%d.java", day));
        if (!Files.exists(classFilePath)) {
            String template = Files.readString(Path.of("src/setup/Day.txt")).replaceAll("%Day%", String.valueOf(day));
            Files.writeString(classFilePath, template);
            System.out.printf("Generated class file at: %s", classFilePath.toAbsolutePath());
        } else {
            System.out.printf("Class file exists at: %s", classFilePath.toAbsolutePath());
        }
    }

    private static void downloadInput(String sessionToken, String year, int day) throws IOException, InterruptedException {
        URI uri = URI.create(String.format("https://adventofcode.com/%s/day/%d/input", year, day));
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder(uri).GET().header("cookie", String.format("session=%s", sessionToken)).build();

        Path file = Paths.get(String.format("data/day%d/input.txt", day));
        file.toFile().getParentFile().mkdirs();
        httpClient.send(req, HttpResponse.BodyHandlers.ofFile(file));

        System.out.print("Link to puzzle: ");
        System.out.printf("https://adventofcode.com/%s/day/%d%n", year, day);
    }

    private static int getDay() {
        System.out.print("Setup for day: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}

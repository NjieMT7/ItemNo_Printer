import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ItemNumberConfig {

    private static final Path CONFIG_PATH = Paths.get("config.properties");

    public String getItemNumber(String location) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(CONFIG_PATH)) {
            String line = "";
            for (int i = 0; i <= getLineIndex(location); i++) {
                line = reader.readLine();
            }
            return line.trim();
        }
    }

    public void addToItemNumber(String location, int amount) throws IOException {
        List<String> lines = Files.readAllLines(CONFIG_PATH);
        int lineIndex = getLineIndex(location);
        String currentItemNumber = lines.get(lineIndex).trim();
        long nextItemNumber = Long.parseLong(currentItemNumber) + amount;
        lines.set(lineIndex, String.format("%0" + currentItemNumber.length() + "d", nextItemNumber));
        Files.write(CONFIG_PATH, lines);
    }

    private int getLineIndex(String location) {
        return switch (location) {
            case "Main" -> 0;
            case "Ebay" -> 1;
            case "Clothing" -> 2;
            default -> throw new IllegalArgumentException("Unknown location: " + location);
        };
    }
}

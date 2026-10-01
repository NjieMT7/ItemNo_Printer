import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ItemNumberConfig {

    private static final Path CONFIG_PATH = Paths.get("config.properties");

    public String getItemNumber(String location) throws IOException {
        if (!Files.exists(CONFIG_PATH)) {
            throw new IOException("config.properties was not found in the application working directory.");
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
            properties.load(input);
        }

        String propertyName;
        if ("Ebay".equals(location)) {
            propertyName = "ebayItemNumber";
        } else if ("Main".equals(location)) {
            propertyName = "currentItemNumber";
        } else if ("Clothing".equals(location)) {
            propertyName = "jacketsItemNumber";
        } else {
            throw new IllegalArgumentException("Unknown item number location: " + location);
        }

        String itemNumber = properties.getProperty(propertyName);
        if (itemNumber == null || itemNumber.trim().isEmpty()) {
            throw new IOException("Set " + propertyName + " in config.properties.");
        }
        itemNumber = itemNumber.trim();

        if ("Main".equals(location) && "220000000".equals(itemNumber)) {
            throw new IOException("The available Main item numbers starting with 21 have been used.");
        }
        if ("Main".equals(location) && !itemNumber.matches("21\\d{7}")) {
            throw new IOException("Set currentItemNumber in config.properties to a 9-digit number starting with 21.");
        }
        return itemNumber;
    }

    public void updateItemNumber(String location, String itemNumber) throws IOException {
        if (!Files.exists(CONFIG_PATH)) {
            throw new IOException("config.properties was not found in the application working directory.");
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
            properties.load(input);
        }

        String propertyName = getPropertyName(location);
        properties.setProperty(propertyName, itemNumber);
        try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
            properties.store(output, "Item number configuration");
        }
    }

    private String getPropertyName(String location) {
        if ("Ebay".equals(location)) {
            return "ebayItemNumber";
        } else if ("Main".equals(location)) {
            return "currentItemNumber";
        } else if ("Clothing".equals(location)) {
            return "jacketsItemNumber";
        }
        throw new IllegalArgumentException("Unknown item number location: " + location);
    }
}

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EplLabelPrinter {

    private static final Path EPL_PATH = Paths.get("itemnum.epl");
    private static final String ITEM_NUMBER_PLACEHOLDER = "${ITEM_NUMBER}";
    private static final String DATE_PLACEHOLDER = "${DATE}";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public void printLabels(String firstItemNumber, int quantity) throws IOException {
        String template = Files.readString(EPL_PATH, StandardCharsets.US_ASCII);
        long firstNumber = Long.parseLong(firstItemNumber);
        String currentDate = LocalDate.now().format(DATE_FORMAT);

        try (OutputStream printer = new ProcessBuilder("lpr", "-P", "zebzeb").start().getOutputStream()) {
            for (int i = 0; i < quantity; i++) {
                String itemNumber = String.format("%0" + firstItemNumber.length() + "d", firstNumber + i);
                printer.write(template.replace(ITEM_NUMBER_PLACEHOLDER, itemNumber)
                        .replace(DATE_PLACEHOLDER, currentDate)
                        .getBytes(StandardCharsets.US_ASCII));
            }
        }
    }
}

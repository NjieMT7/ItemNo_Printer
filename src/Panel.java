import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.math.BigInteger;

public class Panel extends JPanel {

    String[] locations = {"Ebay", "Main", "Clothing"};



    public Panel() {

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel label = new JLabel("LDR Item No Printer", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        add(label, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        Font controlFont = new Font("Arial", Font.PLAIN, 18);

        JComboBox<String> locDropdown = new JComboBox<>(locations);
        locDropdown.setFont(controlFont);

        JTextField num = new JTextField();
        num.setFont(controlFont);

        JButton button = new JButton("Print");
        button.setFont(controlFont);

        JLabel locationLabel = new JLabel("Location for each item number:");
        locationLabel.setFont(controlFont);
        formPanel.add(locationLabel);
        formPanel.add(locDropdown);

        JLabel quantityLabel = new JLabel("<html>Number of item numbers<br>to print:</html>");
        quantityLabel.setFont(controlFont);
        formPanel.add(quantityLabel);
        formPanel.add(num);

        formPanel.add(new JLabel(""));
        formPanel.add(button);

        add(formPanel, BorderLayout.CENTER);

        button.addActionListener(e -> {
            String location = (String) locDropdown.getSelectedItem();
            if(location == null) {
                JOptionPane.showMessageDialog(this, "Please select a location for each item number!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int quantity;
                try {
                    quantity = Integer.parseInt(num.getText().trim());
                    if (quantity <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(this, "Please enter a positive whole number for the quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    ItemNumberConfig config = new ItemNumberConfig();
                    String itemNumber = config.getItemNumber(location);
                    if (!itemNumber.matches("\\d+")) {
                        JOptionPane.showMessageDialog(this,
                                "The configured item number for " + location + " must contain only digits.",
                                "Configuration Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    BigInteger firstNumber = new BigInteger(itemNumber);
                    BigInteger lastNumber = firstNumber.add(BigInteger.valueOf(quantity - 1L));
                    String lastItemNumber = formatItemNumber(lastNumber, itemNumber.length());
                    String nextItemNumber = formatItemNumber(lastNumber.add(BigInteger.ONE), itemNumber.length());
                    if (lastItemNumber.length() != itemNumber.length()
                            || nextItemNumber.length() != itemNumber.length()
                            || ("Main".equals(location) && (!lastItemNumber.matches("21\\d{7}")
                            || !nextItemNumber.matches("21\\d{7}|220000000")))) {
                        JOptionPane.showMessageDialog(this,
                                "The requested quantity exceeds the valid item number range for " + location + ".",
                                "Configuration Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    System.out.println("Testing item numbers for " + location + ":");
                    for (int i = 0; i < quantity; i++) {
                        String generatedItemNumber = formatItemNumber(firstNumber.add(BigInteger.valueOf(i)), itemNumber.length());
                        System.out.println("Item " + (i + 1) + ": " + generatedItemNumber);
                    }
                    config.updateItemNumber(location, nextItemNumber);
                    JOptionPane.showMessageDialog(this,
                            "Item numbers: " + itemNumber + " - " + lastItemNumber
                                    + System.lineSeparator() + "Next item number: " + nextItemNumber
                                    + System.lineSeparator() + "Quantity: " + quantity,
                            location + " item number", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(this, exception.getMessage(), "Configuration Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                int fontSize = Math.max(10, Math.min(48,
                        (int) Math.round(Math.min(getWidth() * 0.03, getHeight() * 0.045))));
                locDropdown.setFont(locDropdown.getFont().deriveFont((float) fontSize));
                button.setFont(button.getFont().deriveFont((float) fontSize));
            }
        });
    }

    private String formatItemNumber(BigInteger number, int width) {
        String formattedNumber = number.toString();
        return "0".repeat(Math.max(0, width - formattedNumber.length())) + formattedNumber;
    }
}
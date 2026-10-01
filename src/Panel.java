import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Panel extends JPanel {

    public Panel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel label = new JLabel("LDR Item No Printer", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        Font controlFont = new Font("Arial", Font.PLAIN, 18);
        JComboBox<String> locDropdown = new JComboBox<>(new String[]{"Ebay", "Main", "Clothing"});
        locDropdown.setFont(controlFont);
        JTextField num = new JTextField();
        num.setFont(controlFont);
        JButton button = new JButton("Print");
        button.setFont(controlFont);

        JLabel locationLabel = new JLabel("Location for each item number:");
        locationLabel.setFont(controlFont);
        JLabel quantityLabel = new JLabel("<html>Number of item numbers<br>to print:</html>");
        quantityLabel.setFont(controlFont);
        formPanel.add(locationLabel);
        formPanel.add(locDropdown);
        formPanel.add(quantityLabel);
        formPanel.add(num);
        formPanel.add(new JLabel(""));
        formPanel.add(button);
        add(formPanel, BorderLayout.CENTER);

        button.addActionListener(e -> {
            String location = (String) locDropdown.getSelectedItem();
            int quantity;
            try {
                quantity = Integer.parseInt(num.getText().trim());
                if (quantity <= 0) throw new NumberFormatException();
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(this, "Enter a positive whole-number quantity.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                ItemNumberConfig config = new ItemNumberConfig();
                String itemNumber = config.getItemNumber(location);
                new EplLabelPrinter().printLabels(itemNumber, quantity);
                config.addToItemNumber(location, quantity);
                JOptionPane.showMessageDialog(this,
                        "Printed " + quantity + " item numbers starting at " + itemNumber + ".",
                        location + " item number", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage(),
                        "Printing Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
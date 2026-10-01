import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
String [] Location = {"Ebay", "Main", "Clothing"};


    public Frame() {

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        int screenWidth = toolkit.getScreenSize().width;
        int screenHeight = toolkit.getScreenSize().height;

        int frameWidth = screenWidth * 2 / 4;
        int frameHeight = screenHeight * 2 / 4;

        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        JLabel label = new JLabel("LDR Item No Printer");

        label.setFont(new Font("Arial", Font.BOLD, 24));

        JButton button = new JButton("Print");

        JTextField Num = new JTextField();

        JComboBox<String> LocDropdown = new JComboBox<>(Location);

        panel.add(label);
        panel.add(button);
        panel.add(Num);
        panel.add(LocDropdown);


        setLayout(new BorderLayout());

        add(panel, BorderLayout.NORTH);
        add(button, BorderLayout.EAST);
        add(Num, BorderLayout.WEST);
        add(LocDropdown,BorderLayout.CENTER);

    }



}


import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("LDR Item No Printer");

        Panel panel = new Panel();

        frame.add(panel);

        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

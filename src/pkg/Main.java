package pkg;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        // Ensure GUI construction happens on the Event-Dispatching thread for thread-safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameWindow gameWindow = new GameWindow();
                gameWindow.setVisible(true);
            }
        });
    }
}
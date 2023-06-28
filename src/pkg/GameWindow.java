package pkg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame {
    private JPanel container;
    private CardLayout cardLayout;

    public GameWindow() {
        super("Game Window");
        setSize(750, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the container panel and set its layout
        container = new JPanel();
        cardLayout = new CardLayout();
        container.setLayout(cardLayout);

        // Initialize the game panels
        MemoryGame memoryGame = new MemoryGame();

        // Add the game panels to the container
        container.add(memoryGame, "MemoryGame");

        // Create the game selection button
        JButton memoryGameButton = new JButton("Memory Game");
        memoryGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container, "MemoryGame");
                memoryGame.showColors();
            }
        });

        // Create a panel to hold the game selection buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(memoryGameButton);

        // Add the container and button panels to the window
        add(container, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Center the window and make it visible
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
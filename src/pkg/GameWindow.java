package pkg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame {
    private JPanel container;
    private CardLayout cardLayout;

    public GameWindow() {
        super("Citus App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container = new JPanel();
        cardLayout = new CardLayout();
        container.setLayout(cardLayout);
        JButton memoryGameButton = new JButton("Start Game");
        MemoryGame memoryGame = new MemoryGame(memoryGameButton);
        container.add(memoryGame, "MemoryGame");


        memoryGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container, "MemoryGame");
                memoryGame.showColors();
                memoryGame.startGame();
                memoryGameButton.setVisible(false);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(memoryGameButton);
        JLabel titleLabel = new JLabel("Citius");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        add(titleLabel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
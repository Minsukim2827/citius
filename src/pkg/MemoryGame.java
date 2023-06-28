package pkg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.border.EmptyBorder;
public class MemoryGame extends JPanel {
    private JButton[][] gridButtons;
    private Color[][] gridColors;
    private Random random = new Random();
    private List<Point> correctSequence = new ArrayList<>();  // Store the correct sequence of colors
    private List<Point> userSequence = new ArrayList<>();  // Store the user's sequence of colors
    private int score = 0;
    private JLabel scoreLabel;
    private JLabel messageLabel;  // Add this JLabel to display messages to the user
    private static final int GRID_SIZE = 300;
    private static final int BUTTON_SIZE = GRID_SIZE / 3;

    public void startGame() {
        GameTimer gameTimer = new GameTimer(60, new GameTimer.GameTimerListener() {
            @Override
            public void onTimeOut() {
                // This is called when the timer runs out.
                // You can add code here to end the game and show the score.
                // Make sure to access Swing components on the Event Dispatch Thread using SwingUtilities.invokeLater().
            }
        });
        gameTimer.startTimer();
        // You can also add code here to reset the game state if necessary.
    }

    public MemoryGame() {
        setPreferredSize(new Dimension(300, 300));
        setLayout(new BorderLayout());
        messageLabel = new JLabel("", SwingConstants.CENTER);
        add(messageLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        gridPanel.setPreferredSize(new Dimension(300, 300));
        generateGridColors();
        initializeButtons(gridPanel);

        JPanel centerPanel = new JPanel();
        centerPanel.add(gridPanel);
        centerPanel.setBorder(new EmptyBorder(50, 50, 50, 50)); // Add a margin of 50px
        add(centerPanel, BorderLayout.CENTER);
        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        add(scoreLabel, BorderLayout.SOUTH);
    }

    private void generateGridColors() {
        gridColors = new Color[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gridColors[i][j] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            }
        }
    }
    private void resetGridColors() {
        Timer delayTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton[] buttonRow : gridButtons) {
                    for (JButton button : buttonRow) {
                        button.setBackground(Color.GRAY);
                    }
                }
            }
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void initializeButtons(JPanel gridPanel) {
        gridButtons = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton();
                button.setBackground(Color.GRAY);
                int finalI = i;
                int finalJ = j;
                button.setEnabled(false);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color originalColor = button.getBackground();
                        button.setBackground(gridColors[finalI][finalJ]);

                        // Flash color for a brief moment before resetting it
                        Timer flashTimer = new Timer(500, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                button.setBackground(originalColor);
                            }
                        });
                        flashTimer.setRepeats(false);
                        flashTimer.start();

                        userSequence.add(new Point(finalI, finalJ));
                        checkSequence();
                    }
                });
                gridButtons[i][j] = button;
                gridPanel.add(button);
            }

        }
    }
    // Modify showColors to flash the color then reset it
    private int currentSequenceLength = 0;
    public void showColors() {
        disableButtons();
        // Clear userSequence right before displaying a new sequence
        userSequence.clear();
        messageLabel.setText("");
        // Increase sequence by 1 each time showColors() is called
        currentSequenceLength++;

        int x = random.nextInt(3);
        int y = random.nextInt(3);
        correctSequence.add(new Point(x, y));

        Timer timer = new Timer(500, null);
        timer.addActionListener(new ActionListener() {
            int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                resetGridColors();
                if (index < correctSequence.size()) {
                    Point point = correctSequence.get(index);
                    JButton button = gridButtons[point.x][point.y];
                    Color originalColor = button.getBackground();
                    button.setBackground(gridColors[point.x][point.y]);

                    // Flash color for a brief moment before resetting it
                    Timer flashTimer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            button.setBackground(originalColor);
                        }
                    });
                    flashTimer.setRepeats(false);
                    flashTimer.start();

                    index++;
                } else {
                    timer.stop();
                    enableButtons();
                }
            }
        });
        timer.start();
    }


    public void checkSequence() {
        boolean isCorrect = true;

        List<Point> correctSubSequence = correctSequence.subList(0, userSequence.size());
        if (!userSequence.equals(correctSubSequence)) {
            isCorrect = false;
        }

        if (isCorrect && userSequence.size() == correctSequence.size()) {
            score++; // Increment the score
            scoreLabel.setText("Score: " + score); // Update the score label
            messageLabel.setText("Correct!");
            resetGridColors();
            disableButtons();
            Timer delayTimer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showColors();
                }
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        } else if (!isCorrect) {
            messageLabel.setText("Incorrect Sequence");
            userSequence.clear();
            resetGridColors();
            disableButtons();

            // After incorrect input, delay the sequence display for a moment
            Timer delayTimer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showCurrentColors();
                }
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }





    private void resetGame() {
        // Reset the game
        disableButtons();
        userSequence.clear();  // Clear the user's sequence

        // Show the sequence again after a 1 second delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        showColors();
    }
    public void showCurrentColors() {
        // Clear userSequence right before displaying the current sequence
        userSequence.clear();
        messageLabel.setText("");
        resetGridColors();
        disableButtons();
        Timer timer = new Timer(500, null);
        timer.addActionListener(new ActionListener() {
            int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < correctSequence.size()) {
                    Point point = correctSequence.get(index);
                    JButton button = gridButtons[point.x][point.y];
                    Color originalColor = button.getBackground();
                    button.setBackground(gridColors[point.x][point.y]);

                    // Flash color for a brief moment before resetting it
                    Timer flashTimer = new Timer(250, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            button.setBackground(originalColor);
                        }
                    });
                    flashTimer.setRepeats(false);
                    flashTimer.start();

                    index++;
                } else {
                    timer.stop();
                    enableButtons();
                }
            }
        });
        timer.start();
    }
    private void disableButtons(){
        // Disable all buttons at the start
        for (JButton[] buttonRow : gridButtons) {
            for (JButton button : buttonRow) {
                button.setEnabled(false);
                button.setBackground(Color.GRAY);
            }
        }
    }
    private void enableButtons(){
        // enable buttons
        for (JButton[] buttonRow : gridButtons) {
            for (JButton button : buttonRow) {
                button.setEnabled(true);
                button.setBackground(Color.GRAY);
            }
        }
    }

    private void endGame() {
        // The game ends here. You can add code to handle this, such as showing the user's score and restarting the game.
    }


}

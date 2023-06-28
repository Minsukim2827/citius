package pkg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class MemoryGame extends JPanel {
    private JButton[][] gridButtons;
    private Color[][] gridColors;
    private Random random = new Random();
    private List<Point> correctSequence = new ArrayList<>();  // Store the correct sequence of colors
    private List<Point> userSequence = new ArrayList<>();  // Store the user's sequence of colors
    private JLabel messageLabel;  // Add this JLabel to display messages to the user


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
        setLayout(new GridLayout(3, 3));
        generateGridColors();
        initializeButtons();
        messageLabel = new JLabel();
        add(messageLabel, BorderLayout.NORTH);
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

    private void initializeButtons() {
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
                add(button);
            }

        }
    }
    // Modify showColors to flash the color then reset it
    private int currentSequenceLength = 0;
    public void showColors() {
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
                    for (JButton[] buttonRow : gridButtons) {
                        for (JButton button : buttonRow) {
                            button.setEnabled(true);
                        }
                    }
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
            messageLabel.setText("Correct!");
            resetGridColors();
            Timer delayTimer = new Timer(1000, new ActionListener() {
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

            // After incorrect input, delay the sequence display for a moment
            Timer delayTimer = new Timer(1000, new ActionListener() {
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
        for (JButton[] buttonRow : gridButtons) {
            for (JButton button : buttonRow) {
                button.setEnabled(false);  // Disable the buttons
                button.setBackground(Color.GRAY);  // Reset the button color
            }
        }
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
        // Disable all buttons at the start
        for (JButton[] buttonRow : gridButtons) {
            for (JButton button : buttonRow) {
                button.setEnabled(false);
            }
        }
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
                    for (JButton[] buttonRow : gridButtons) {
                        for (JButton button : buttonRow) {
                            button.setEnabled(true);
                        }
                    }
                }
            }
        });
        timer.start();
    }

    private void endGame() {
        // The game ends here. You can add code to handle this, such as showing the user's score and restarting the game.
    }


}

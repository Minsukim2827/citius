package pkg;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameTimer {
    private int timeLeft;
    private GameTimerListener listener;
    private javax.swing.Timer swingTimer;

    public GameTimer(int timeLeft, GameTimerListener listener) {
        this.timeLeft = timeLeft;
        this.listener = listener;
    }

    public void startTimer() {
        swingTimer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                if (timeLeft <= 0) {
                    swingTimer.stop();
                    listener.onTimeOut();
                }
            }
        });
        swingTimer.start();
    }

    public interface GameTimerListener {
        void onTimeOut();
    }
}

package utils;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Clock implements Runnable {
    private final Label clockLabel;
    private boolean flag = true;

    public Clock(Label clockLabel) {
        this.clockLabel = clockLabel;
    }

    @Override
    public void run() {
        while (flag) {
            LocalTime currentTime = LocalTime.now();
            final String time = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            Platform.runLater(() -> clockLabel.setText(time));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void terminate() {
        flag = false;
    }
}
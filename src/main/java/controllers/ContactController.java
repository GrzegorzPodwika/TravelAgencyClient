package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import utils.Clock;
import utils.SceneCreator;

import java.io.IOException;

public class ContactController {
    @FXML public Label clockLabel;

    private Clock clk;
    private Thread th;

    @FXML
    public void initialize() {
        clk = new Clock(clockLabel);
        th = new Thread(clk);
        th.start();
    }

    @FXML
    public void logOutButton(MouseEvent event) throws IOException {
        Main.setUser(null);
        SceneCreator.launchScene("LogInScene.fxml");
        shutdown();
    }

    @FXML
    public void goBackButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("UserScene.fxml");
        shutdown();
    }

    public void shutdown() {
        clk.terminate();
    }
}

package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import utils.Clock;
import utils.SceneCreator;

import java.io.IOException;

import static utils.Constants.LOGIN_SCENE;
import static utils.Constants.USER_SCENE;

public class ContactController {
    @FXML public Label clockLabel;

    private Clock clk;

    @FXML
    public void initialize() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    @FXML
    public void onLogoutClick() {
        Main.setUser(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    @FXML
    public void onGoBackClick(){
        SceneCreator.launchScene(USER_SCENE);
        shutdown();
    }

    public void shutdown() {
        clk.terminate();
    }
}

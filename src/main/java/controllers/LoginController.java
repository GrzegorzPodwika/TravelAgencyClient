package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.java.Main;
import main.java.utils.SceneCreator;

import java.io.IOException;

public class LoginController {
    @FXML TextField usernameBox;
    @FXML PasswordField passwordBox;
    @FXML Label errorLabel;

    @FXML
    public void loginButton(MouseEvent event) {
        if(!usernameBox.getText().isEmpty() && !passwordBox.getText().isEmpty()) {
            tryLogin();
        }else{
            errorLabel.setText("Pole nazwa użytkownika i hasło nie mogą być puste!");
        }
    }

    private void tryLogin() {

    }

    public void registerButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("RegisterScene.fxml", Main.getUser());
    }
}

package controllers;

import backend.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import utils.Clock;
import utils.SceneCreator;

import java.io.IOException;

public class UserController {

    @FXML public Label clockLabel;
    @FXML public Label helloUser;
    @FXML public Label nameLabel;
    @FXML public Label surnameLabel;
    @FXML public Label nickLabel;
    @FXML public Label emailLabel;
    @FXML public Label countLabel;

    private Thread th;
    private Clock clk;
    private final User activeUser = Main.getUser();


    @FXML
    public void initialize() {
        clk = new Clock(clockLabel);
        th = new Thread(clk);
        th.start();

        putUserCredentialsIntoList();

    }

    private void putUserCredentialsIntoList() {
        helloUser.setText("Witaj, " + activeUser.getName());
        nameLabel.setText(activeUser.getName());
        surnameLabel.setText(activeUser.getSurname());
        nickLabel.setText(activeUser.getNick());
        emailLabel.setText(activeUser.getEmail());
    }

    //done
    @FXML
    public void logOutButton(){
        Main.setUser(null);
        SceneCreator.launchScene("LogInScene.fxml");
        shutdown();
    }

    //done
    @FXML
    public void editButton(){
        SceneCreator.launchScene("EditCredensialsScene.fxml");
        shutdown();
    }

    @FXML
    public void viewToursButton(){
        SceneCreator.launchScene("ViewToursScene.fxml");
        shutdown();
    }

    @FXML
    public void viewReservationButton() {
        SceneCreator.launchScene("ManageToursScene.fxml");
        shutdown();
    }

    @FXML
    public void removeReservationButton() {
        SceneCreator.launchScene("RemoveReservationScene.fxml");
        shutdown();
    }

    //done
    @FXML
    public void contactButton() {
        SceneCreator.launchScene("ContactScene.fxml");
        shutdown();
    }


    public void shutdown(){
        clk.terminate();
    }
}
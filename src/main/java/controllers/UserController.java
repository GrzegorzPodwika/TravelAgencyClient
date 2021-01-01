package controllers;

import backend.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.Clock;
import utils.SceneCreator;

import static utils.Constants.*;

public class UserController {

    @FXML public Label clockLabel;
    @FXML public Label helloUser;
    @FXML public Label nameLabel;
    @FXML public Label surnameLabel;
    @FXML public Label nickLabel;
    @FXML public Label emailLabel;
    @FXML public Label countLabel;

    private Clock clk;
    private final User activeUser = Main.getUser();


    @FXML
    public void initialize() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
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
    public void onLogoutClick(){
        Main.setUser(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    //done
    @FXML
    public void onEditUserCredentialsClick(){
        SceneCreator.launchScene(EDIT_USER_CREDENTIALS_SCENE);
        shutdown();
    }

    //done
    @FXML
    public void onViewToursClick(){
        SceneCreator.launchScene(VIEW_TOURS_SCENE);
        shutdown();
    }

    @FXML
    public void onManageReservationClick() {
        SceneCreator.launchScene(MANAGE_RESERVATIONS_SCENE);
        shutdown();
    }


    //done
    @FXML
    public void onViewCompanyContactClick() {
        SceneCreator.launchScene(CONTACT_SCENE);
        shutdown();
    }


    public void shutdown(){
        clk.terminate();
    }
}
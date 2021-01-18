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

    @FXML public Label labelName;
    @FXML public Label labelSurname;
    @FXML public Label labelAge;
    @FXML public Label labelAddress;
    @FXML public Label labelZipcode;
    @FXML public Label labelCity;
    @FXML public Label labelPhoneNumber;
    @FXML public Label labelEmail;


    private Clock clk;
    private final User activeUser = Main.getUser();


    @FXML
    public void initialize() {
        initClock();

        putUserCredentialsIntoList();

    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void putUserCredentialsIntoList() {
        helloUser.setText("Witaj, " + activeUser.getName());

        labelName.setText(labelName.getText() + activeUser.getName());
        labelSurname.setText(labelSurname.getText() + activeUser.getSurname());
        labelAge.setText(labelAge.getText() + activeUser.getAge());
        labelAddress.setText(labelAddress.getText() + activeUser.getAddress());
        labelZipcode.setText(labelZipcode.getText() + activeUser.getZipcode());
        labelCity.setText(labelCity.getText() + activeUser.getCity());
        labelPhoneNumber.setText(labelPhoneNumber.getText() + activeUser.getPhoneNumber());
        labelEmail.setText(labelEmail.getText() + activeUser.getEmail());
    }

    @FXML
    public void onLogoutClick(){
        Main.setUser(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    @FXML
    public void onEditUserCredentialsClick(){
        SceneCreator.launchScene(EDIT_USER_CREDENTIALS_SCENE);
        shutdown();
    }

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

    @FXML
    public void onViewCompanyContactClick() {
        SceneCreator.launchScene(CONTACT_SCENE);
        shutdown();
    }


    public void shutdown(){
        clk.terminate();
    }
}
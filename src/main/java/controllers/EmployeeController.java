package controllers;

import backend.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.Clock;
import utils.SceneCreator;

import static utils.Constants.*;

public class EmployeeController {
    @FXML public Label labelHelloUser;
    @FXML public Label labelEmail;
    @FXML public Label labelSurname;
    @FXML public Label labelName;
    @FXML public Label labelNick;
    @FXML public Label labelSalary;
    @FXML public Label clockLabel;

    private Clock clk;
    private final Employee activeEmployee = Main.getEmployee();

    @FXML
    public void initialize() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();

        putEmployeeCredentialsIntoList();
    }

    private void putEmployeeCredentialsIntoList() {
        labelHelloUser.setText("Witaj, " + activeEmployee.getName());
        labelName.setText(activeEmployee.getName());
        labelSurname.setText(activeEmployee.getSurname());
        labelEmail.setText(activeEmployee.getEmail());
        labelNick.setText(activeEmployee.getNick());
        double salary = activeEmployee.getHourlyRate() * activeEmployee.getNumberOfWorkedHours() + activeEmployee.getBonus();
        labelSalary.setText(String.valueOf(salary));
    }

    @FXML
    public void onLogoutClick() {
        Main.setEmployee(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    @FXML
    public void onEditDataClick() {
        SceneCreator.launchScene(EDIT_EMPLOYEE_CREDENTIALS_SCENE);
        shutdown();
    }

    @FXML
    public void onToursClick() {
        SceneCreator.launchScene(MANAGE_TOURS_SCENE);
        shutdown();
    }

    @FXML
    public void onHotelsClick() {
        SceneCreator.launchScene(HOTEL_SCENE);
        shutdown();
    }

    @FXML
    public void onTourGuidesClick() {
        SceneCreator.launchScene(TOUR_GUIDE_SCENE);
        shutdown();
    }

    @FXML
    public void onCarriersClick() {
        SceneCreator.launchScene(CARRIER_SCENE);
        shutdown();
    }

    @FXML
    public void onClientsClick() {
        SceneCreator.launchScene(MANAGE_USERS_SCENE);
        shutdown();
    }


    private void shutdown() {
        clk.terminate();
    }
}

package controllers;

import backend.model.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.Clock;
import utils.SceneCreator;
import utils.TravelUtils;

import static utils.Constants.*;
import static utils.TravelUtils.*;

public class EmployeeController {
    @FXML public Label labelHelloUser;
    @FXML public Label labelEmail;
    @FXML public Label labelSurname;
    @FXML public Label labelName;
    @FXML public Label labelNick;
    @FXML public Label labelHourlyPay;
    @FXML public Label labelWorkedHours;
    @FXML public Label labelBonus;
    @FXML public Label labelSalary;
    @FXML public Label clockLabel;

    private Clock clk;
    private final Employee activeEmployee = Main.getEmployee();

    @FXML
    public void initialize() {
        initClock();

        putEmployeeCredentialsIntoList();
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void putEmployeeCredentialsIntoList() {
        labelHelloUser.setText("Witaj, " + activeEmployee.getName());
        labelName.setText(labelName.getText() +  activeEmployee.getName());
        labelSurname.setText(labelSurname.getText() +  activeEmployee.getSurname());
        labelEmail.setText(labelEmail.getText() + activeEmployee.getEmail());
        labelNick.setText(labelNick.getText() + activeEmployee.getNick());
        labelHourlyPay.setText(labelHourlyPay.getText() + activeEmployee.getHourlyRate());
        labelWorkedHours.setText(labelWorkedHours.getText() + activeEmployee.getNumberOfWorkedHours());
        labelBonus.setText(labelBonus.getText() + activeEmployee.getBonus());
        double salary = activeEmployee.getHourlyRate() * activeEmployee.getNumberOfWorkedHours() + activeEmployee.getBonus();
        salary = roundOff(salary);
        labelSalary.setText(labelSalary.getText() + salary);
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
    public void onTransportClick() {
        SceneCreator.launchScene(TRANSPORT_SCENE);
        shutdown();
    }

    @FXML
    public void onAdditionalServiceClick( ) {
        SceneCreator.launchScene(ADDITIONAL_SERVICE_SCENE);
        shutdown();
    }

    @FXML
    public void onAttractionClick( ) {
        SceneCreator.launchScene(ATTRACTION_SCENE);
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

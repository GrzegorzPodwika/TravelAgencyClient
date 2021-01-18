package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.EmployeeService;
import backend.model.Employee;
import backend.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Clock;
import utils.SceneCreator;

import static utils.Constants.*;

public class EditEmployeeCredentialsController {

    @FXML
    public Label clockLabel;
    @FXML
    public Label errorLabelAge;
    @FXML
    public Label errorLabelPhone;
    @FXML
    public Label errorLabelEmail;
    @FXML
    public Label errorLabelName;
    @FXML
    public Label errorLabelSurname;
    @FXML
    public Label errorLabelCity;

    @FXML
    public Label labelLogin;
    @FXML
    public Label labelPassword;
    @FXML
    public Label labelName;
    @FXML
    public Label labelSurname;
    @FXML
    public Label labelAge;
    @FXML
    public Label labelAddress;
    @FXML
    public Label labelZipcode;
    @FXML
    public Label labelCity;
    @FXML
    public Label labelPhoneNumber;
    @FXML
    public Label labelEmail;

    @FXML
    public TextField inputLogin;
    @FXML
    public TextField inputPassword;
    @FXML
    public TextField inputName;
    @FXML
    public TextField inputSurname;
    @FXML
    public TextField inputAge;
    @FXML
    public TextField inputAddress;
    @FXML
    public TextField inputZipcode;
    @FXML
    public TextField inputCity;
    @FXML
    public TextField inputPhoneNumber;
    @FXML
    public TextField inputEmail;

    private Clock clk;
    private final Employee activeEmployee = Main.getEmployee();
    private final EmployeeService employeeService = AgencyServiceGenerator.createService(EmployeeService.class);

    @FXML
    public void initialize() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();

        putEmployeeCredentialsIntoList();
        setInputFieldsListeners();
    }

    private void putEmployeeCredentialsIntoList() {
        labelLogin.setText("Nazwa użytkownika " + activeEmployee.getNick());
        labelPassword.setText("Hasło  " + activeEmployee.getPassword());
        labelName.setText("Imię " + activeEmployee.getName());
        labelSurname.setText("Nazwisko " + activeEmployee.getSurname());
        labelAge.setText("Wiek " + activeEmployee.getAge());
        labelAddress.setText("Adres " + activeEmployee.getAddress());
        labelZipcode.setText("Kod pocztowy " + activeEmployee.getZipcode());
        labelCity.setText("Miasto " + activeEmployee.getCity());
        labelPhoneNumber.setText("Nr telefonu  " + activeEmployee.getPhoneNumber());
        labelEmail.setText("Email  " + activeEmployee.getEmail());
    }

    private void setInputFieldsListeners() {
        inputAge.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    Integer.parseInt(newValue);
                    errorLabelAge.setText("");
                } catch (NumberFormatException e) {
                    errorLabelAge.setText(ERROR_NOT_A_NUMBER);
                }

            } else {
                errorLabelAge.setText("");
            }
        });
        inputPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (inputPhoneNumber.getText().length() > 9) {
                    String sub = inputPhoneNumber.getText().substring(0, 9);
                    inputPhoneNumber.setText(sub);
                } else {
                    try {
                        var isNumber = Integer.parseInt(newValue);
                        if (newValue.length() != 9) {
                            errorLabelPhone.setText(ERROR_WRONG_PHONE);
                        } else
                            errorLabelPhone.setText("");
                    } catch (NumberFormatException e) {
                        errorLabelPhone.setText(ERROR_NOT_A_NUMBER);
                    }
                }
            } else {
                errorLabelPhone.setText("");
            }
        });
        inputEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (!newValue.matches("^([a-zA-Z0-9_\\.\\-+])+@[a-zA-Z0-9-.]+\\.[a-zA-Z0-9-]{2,}$")) {
                    errorLabelEmail.setText(ERROR_NOT_A_EMAIL);
                } else {
                    errorLabelEmail.setText("");
                }
            } else {
                errorLabelEmail.setText("");
            }
        });

        inputName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (!newValue.matches("^[A-Z]([a-z])+$"))
                    errorLabelName.setText(ERROR_CAPITALIZE_WORD);
                else
                    errorLabelName.setText("");
            } else {
                errorLabelName.setText("");
            }
        });

        inputSurname.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (!newValue.matches("^[A-Z]([a-z])+$"))
                    errorLabelSurname.setText(ERROR_CAPITALIZE_WORD);
                else
                    errorLabelSurname.setText("");
            } else {
                errorLabelSurname.setText("");
            }
        });

        inputCity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (!newValue.matches("^[A-Z]([a-z])+$"))
                    errorLabelCity.setText(ERROR_CAPITALIZE_WORD);
                else
                    errorLabelCity.setText("");
            } else {
                errorLabelCity.setText("");
            }
        });
    }

    @FXML
    public void onLogoutClick() {
        Main.setEmployee(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    @FXML
    public void onGoBackClick() {
        SceneCreator.launchScene(EMPLOYEE_SCENE);
        shutdown();
    }

    @FXML
    public void onConfirmClick() {
        updateEmployeeViaServer();
    }

    private void updateEmployeeViaServer() {
        Employee employeeToUpdate = prepareEmployee();

        var updateCall = employeeService.updateEmployee(employeeToUpdate);
        updateCall.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    var fetchedEmployee = response.body();
                    if (fetchedEmployee != null) {
                        Platform.runLater(() -> {
                            Main.setEmployee(fetchedEmployee);

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Potwierdzenie");
                            alert.setHeaderText(null);
                            alert.setContentText("Dane pracownika zostały pomyślnie zmienione!");
                            alert.setX(750);
                            alert.setY(384);
                            alert.showAndWait();

                            SceneCreator.launchScene("EmployeeScene.fxml");
                            shutdown();
                        });
                    } else {
                        System.out.println("Something went wrong, fetchedEmployee == null!");
                    }
                } else {
                    System.out.println("Server response is not successful! " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable throwable) {
                System.out.println("Server is not available!");
            }
        });
    }

    private Employee prepareEmployee() {
        Employee employeeToUpdate = new Employee();
        employeeToUpdate.setPersonId(activeEmployee.getPersonId());

        if (inputLogin.getText() == null || inputLogin.getText().isEmpty())
            employeeToUpdate.setNick(activeEmployee.getNick());
        else
            employeeToUpdate.setNick(inputLogin.getText());

        if (inputPassword.getText() == null || inputPassword.getText().isEmpty())
            employeeToUpdate.setPassword(activeEmployee.getPassword());
        else
            employeeToUpdate.setPassword(inputPassword.getText());

        if (inputName.getText() == null || inputName.getText().isEmpty() || !errorLabelName.getText().isEmpty())
            employeeToUpdate.setName(activeEmployee.getName());
        else
            employeeToUpdate.setName(inputName.getText());

        if (inputSurname.getText() == null || inputSurname.getText().isEmpty() || !errorLabelSurname.getText().isEmpty())
            employeeToUpdate.setSurname(activeEmployee.getSurname());
        else
            employeeToUpdate.setSurname(inputSurname.getText());

        if (inputAge.getText() == null || inputAge.getText().isEmpty() || !errorLabelAge.getText().isEmpty())
            employeeToUpdate.setAge(activeEmployee.getAge());
        else
            employeeToUpdate.setAge(Integer.parseInt(inputAge.getText()));

        if (inputAddress.getText() == null || inputAddress.getText().isEmpty())
            employeeToUpdate.setAddress(activeEmployee.getAddress());
        else
            employeeToUpdate.setAddress(inputAddress.getText());

        if (inputZipcode.getText() == null || inputZipcode.getText().isEmpty())
            employeeToUpdate.setZipcode(activeEmployee.getZipcode());
        else
            employeeToUpdate.setZipcode(inputZipcode.getText());

        if (inputCity.getText() == null || inputCity.getText().isEmpty() || !errorLabelCity.getText().isEmpty())
            employeeToUpdate.setCity(activeEmployee.getCity());
        else
            employeeToUpdate.setCity(inputCity.getText());

        if (inputPhoneNumber.getText() == null || inputPhoneNumber.getText().isEmpty() || !errorLabelPhone.getText().isEmpty())
            employeeToUpdate.setPhoneNumber(activeEmployee.getPhoneNumber());
        else
            employeeToUpdate.setPhoneNumber(inputPhoneNumber.getText());

        if (inputEmail.getText() == null || inputEmail.getText().isEmpty() || !errorLabelEmail.getText().isEmpty())
            employeeToUpdate.setEmail(activeEmployee.getEmail());
        else
            employeeToUpdate.setEmail(inputEmail.getText());

        return employeeToUpdate;

    }

    private void shutdown() {
        clk.terminate();
    }
}

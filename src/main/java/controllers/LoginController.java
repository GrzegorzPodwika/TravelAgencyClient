package controllers;

import backend.api.*;
import backend.model.Employee;
import backend.model.LoginResponse;
import backend.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.SceneCreator;

import java.io.IOException;

import static utils.Constants.*;

public class LoginController {

    @FXML public TextField usernameBox;
    @FXML public PasswordField passwordBox;
    @FXML public Label errorLabel;

    private final LoginService loginService = AgencyServiceGenerator.createService(LoginService.class);
    private final UserService userService = AgencyServiceGenerator.createService(UserService.class);
    private final EmployeeService employeeService = AgencyServiceGenerator.createService(EmployeeService.class);


    @FXML
    public void loginButton() {
        if(areLabelsNotEmpty()) {
            loginUser();
        }else{
            errorLabel.setText("Pole nazwa użytkownika i hasło nie mogą być puste!");
        }
    }

    private boolean areLabelsNotEmpty() {
        return usernameBox.getText() != null && !usernameBox.getText().isEmpty()
                && passwordBox.getText() != null && !passwordBox.getText().isEmpty();
    }

    private void loginUser() {
        String username = usernameBox.getText();
        String password = passwordBox.getText();

        try {
            var loginResponse = loginService.verifyTypeOfUser(username, password).execute();
            if (loginResponse.isSuccessful()) {
                LoginResponse fetchedResponse = loginResponse.body();

                if (fetchedResponse == LoginResponse.USER)
                    fetchUserAndNavigateToUserScene();
                else if(fetchedResponse == LoginResponse.EMPLOYEE)
                    fetchEmployeeAndNavigateToEmployeeScene();
                else {
                    errorLabel.setText("Błędny login lub hasło!");
                }
            } else {
                errorLabel.setText("Response from server is not successful! " + loginResponse.code());
                System.out.println("Response from server is not successful! " + loginResponse.code());
            }
        } catch (IOException e) {
            errorLabel.setText("Błąd połączenia z serwerem!");
            e.printStackTrace();
        }
    }

    private void fetchUserAndNavigateToUserScene() {
        String username = usernameBox.getText();
        String password = passwordBox.getText();

        var userCall = userService.loginUser(username, password);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User fetchedUser = response.body();

                    if (fetchedUser != null) {
                        Platform.runLater(() -> {
                            Main.setUser(fetchedUser);

                            SceneCreator.launchScene(USER_SCENE);
                        });
                    } else {
                        Platform.runLater(() -> {
                            errorLabel.setText("Fetched user == null!");
                        });
                    }

                } else {
                    Platform.runLater(() -> {
                        errorLabel.setText("Response from server was not successful! " + response.code());
                    });
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Platform.runLater(() -> {
                    errorLabel.setText("Błąd połączenia z serwerem!");
                    throwable.printStackTrace();
                });
            }
        });
    }

    private void fetchEmployeeAndNavigateToEmployeeScene() {
        String username = usernameBox.getText();
        String password = passwordBox.getText();

        var employeeCall = employeeService.loginEmployee(username, password);

        employeeCall.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    Employee fetchedEmployee = response.body();

                    if (fetchedEmployee != null) {
                        Platform.runLater(() -> {
                            Main.setEmployee(fetchedEmployee);

                            SceneCreator.launchScene(EMPLOYEE_SCENE);
                        });
                    } else {
                        Platform.runLater(() -> {
                            errorLabel.setText("Fetched employee == null!");
                        });
                    }

                } else {
                    Platform.runLater(() -> {
                        errorLabel.setText("Response from server is not successful! " + response.code());
                    });
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable throwable) {
                Platform.runLater(() -> {
                    errorLabel.setText("Błąd połączenia z serwerem!");
                    throwable.printStackTrace();
                });
            }
        });
    }

    public void registerButton() {
        SceneCreator.launchScene(REGISTER_SCENE);
    }
}

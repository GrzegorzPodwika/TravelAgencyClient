package controllers;

import backend.api.*;
import backend.model.Employee;
import backend.model.LoginResponse;
import backend.model.Tour;
import backend.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.SceneCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class LoginController {

    private final LoginService loginService = AgencyServiceGenerator.createService(LoginService.class);
    private final UserService userService = AgencyServiceGenerator.createService(UserService.class);
    private final EmployeeService employeeService = AgencyServiceGenerator.createService(EmployeeService.class);

    public ImageView testImage;

    @FXML TextField usernameBox;
    @FXML PasswordField passwordBox;
    @FXML Label errorLabel;

    public void initialize() {

    }

    @FXML
    public void loginButton(MouseEvent event) {
        if(areLabelsNotEmpty()) {
            tryLogin();
        }else{
            errorLabel.setText("Pole nazwa użytkownika i hasło nie mogą być puste!");
        }
    }

    private boolean areLabelsNotEmpty() {
        return !usernameBox.getText().isEmpty() && !passwordBox.getText().isEmpty();
    }

    private void tryLogin() {
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
                System.out.println("COS zlego jest w Response LoginResponse");
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

                            SceneCreator.launchScene("UserScene.fxml");
                        });
                    } else {
                        Platform.runLater(() -> {
                            errorLabel.setText("Fetched user == null!");
                        });
                    }

                } else {
                    Platform.runLater(() -> {
                        errorLabel.setText("Response from server is not successful! " + response.code());
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

                            SceneCreator.launchScene("EmployeeScene.fxml");
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

    public void registerButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("RegisterScene.fxml");
    }
}

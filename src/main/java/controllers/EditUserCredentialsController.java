package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.UserService;
import backend.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Clock;
import utils.SceneCreator;

import java.io.IOException;

import static utils.Constants.*;

public class EditUserCredentialsController {

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
    private final User fetchedUser = Main.getUser();
    private final UserService userService = AgencyServiceGenerator.createService(UserService.class);

    public void initialize() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();

        putUserCredentialsIntoList();
        setInputFieldsListeners();
    }

    private void putUserCredentialsIntoList() {
        labelLogin.setText("Nazwa użytkownika " + fetchedUser.getNick());
        labelPassword.setText("Hasło  " + fetchedUser.getPassword());
        labelName.setText("Imię " + fetchedUser.getName());
        labelSurname.setText("Nazwisko " + fetchedUser.getSurname());
        labelAge.setText("Wiek " + fetchedUser.getAge());
        labelAddress.setText("Adres " + fetchedUser.getAddress());
        labelZipcode.setText("Kod pocztowy " + fetchedUser.getZipcode());
        labelCity.setText("Miasto " + fetchedUser.getCity());
        labelPhoneNumber.setText("Nr telefonu  " + fetchedUser.getPhoneNumber());
        labelEmail.setText("Email  " + fetchedUser.getEmail());
    }

    private void setInputFieldsListeners() {
        inputAge.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (inputAge.getText().length() > 3) {
                    String sub = inputAge.getText().substring(0, 3);
                    inputAge.setText(sub);
                } else  {
                    try {
                        Integer.parseInt(newValue);
                        errorLabelAge.setText("");
                    } catch (NumberFormatException e) {
                        errorLabelAge.setText(ERROR_NOT_A_NUMBER);
                    }
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
    public void logOutButton() {
        Main.setUser(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    @FXML
    public void confirmButton() {
        updateUserViaServer();
    }

    public void updateUserViaServer() {
        User userToUpdate = prepareUser();

        var updateCall = userService.update(userToUpdate);
        updateCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    var fetchedUser = response.body();
                    if (fetchedUser != null) {
                        Platform.runLater(() -> {
                            Main.setUser(fetchedUser);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Potwierdzenie");
                            alert.setHeaderText(null);
                            alert.setContentText("Dane użytkownika zostały pomyślnie zmienione!");
                            alert.setX(750);
                            alert.setY(384);
                            alert.showAndWait();

                            SceneCreator.launchScene(USER_SCENE);
                            shutdown();
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                System.out.println("Błąd od serwera!" + throwable.getMessage());
            }
        });
    }

    private User prepareUser() {
        User userToUpdate = new User();
        userToUpdate.setPersonId(fetchedUser.getPersonId());

        if (inputLogin.getText() == null || inputLogin.getText().isEmpty())
            userToUpdate.setNick(fetchedUser.getNick());
        else
            userToUpdate.setNick(inputLogin.getText());

        if (inputPassword.getText() == null || inputPassword.getText().isEmpty())
            userToUpdate.setPassword(fetchedUser.getPassword());
        else
            userToUpdate.setPassword(inputPassword.getText());

        if (inputName.getText() == null || inputName.getText().isEmpty() || !errorLabelName.getText().isEmpty())
            userToUpdate.setName(fetchedUser.getName());
        else
            userToUpdate.setName(inputName.getText());

        if (inputSurname.getText() == null || inputSurname.getText().isEmpty() || !errorLabelSurname.getText().isEmpty())
            userToUpdate.setSurname(fetchedUser.getSurname());
        else
            userToUpdate.setSurname(inputSurname.getText());

        if (inputAge.getText() == null || inputAge.getText().isEmpty() || !errorLabelAge.getText().isEmpty())
            userToUpdate.setAge(fetchedUser.getAge());
        else
            userToUpdate.setAge(Integer.parseInt(inputAge.getText()));

        if (inputAddress.getText() == null || inputAddress.getText().isEmpty())
            userToUpdate.setAddress(fetchedUser.getAddress());
        else
            userToUpdate.setAddress(inputAddress.getText());

        if (inputZipcode.getText() == null || inputZipcode.getText().isEmpty())
            userToUpdate.setZipcode(fetchedUser.getZipcode());
        else
            userToUpdate.setZipcode(inputZipcode.getText());

        if (inputCity.getText() == null || inputCity.getText().isEmpty() || !errorLabelCity.getText().isEmpty())
            userToUpdate.setCity(fetchedUser.getCity());
        else
            userToUpdate.setCity(inputCity.getText());

        if (inputPhoneNumber.getText() == null || inputPhoneNumber.getText().isEmpty() || !errorLabelPhone.getText().isEmpty())
            userToUpdate.setPhoneNumber(fetchedUser.getPhoneNumber());
        else
            userToUpdate.setPhoneNumber(inputPhoneNumber.getText());

        if (inputEmail.getText() == null || inputEmail.getText().isEmpty() || !errorLabelEmail.getText().isEmpty())
            userToUpdate.setEmail(fetchedUser.getEmail());
        else
            userToUpdate.setEmail(inputEmail.getText());

        return userToUpdate;
    }

    public void goBackButton() {
        SceneCreator.launchScene(USER_SCENE);
        shutdown();
    }

    public void shutdown() {
        clk.terminate();
    }
}

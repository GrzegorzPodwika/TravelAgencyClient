package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.UserService;
import backend.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.SceneCreator;

import java.io.IOException;

public class RegisterController {
    private final UserService userService = AgencyServiceGenerator.createService(UserService.class);

    @FXML public TextField labelNick;
    @FXML public PasswordField labelPassword;
    @FXML public TextField labelName;
    @FXML public TextField labelSurname;
    @FXML public TextField labelAge;
    @FXML public TextField labelAddress;
    @FXML public TextField labelZipcode;
    @FXML public TextField labelCity;
    @FXML public TextField labelPhoneNumber;
    @FXML public TextField labelEmail;
    @FXML public Label errorLabel;

    public void initialize() {
        // do skorzystania
    }

    @FXML
    public void registerButton(MouseEvent event) throws IOException {
        if (areLabelsNotEmpty()) {
            if (labelEmail.getText().indexOf('@') == -1) {
                errorLabel.setText("Podaj poprawny adres E-mail!");
            } else if(isLabelNotANumber(labelAge)) {
                errorLabel.setText("Podaj liczbe jako wiek!");
            } else if(isLabelNotANumber(labelPhoneNumber)) {
                errorLabel.setText("Podaj 9cyfrową liczbe jako telefon!");
            } else {
                communicateWithServer();
            }
        } else {
            errorLabel.setText("Wszystkie pola muszą zostać uzupełnione!");
        }
    }

    private boolean isLabelNotANumber(TextField field) {
        try {
            var age = Integer.parseInt(field.getText());
            return  false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private boolean areLabelsNotEmpty() {
        return !labelNick.getText().isEmpty() && !labelPassword.getText().isEmpty() && !labelName.getText().isEmpty() && !labelSurname.getText().isEmpty() && !labelAge.getText().isEmpty()
                && !labelAddress.getText().isEmpty() && !labelZipcode.getText().isEmpty() && !labelCity.getText().isEmpty() && !labelPhoneNumber.getText().isEmpty();
    }

    @FXML
    public void goBackButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("LogInScene.fxml");
    }

    public void communicateWithServer() throws IOException {
        User user = new User();

        user.setPersonId(0);
        user.setNick(labelNick.getText());
        user.setPassword(labelPassword.getText());
        user.setName(labelName.getText());
        user.setSurname(labelSurname.getText());
        user.setAge(Integer.parseInt(labelAge.getText()));
        user.setAddress(labelAddress.getText());
        user.setCity(labelCity.getText());
        user.setZipcode(labelZipcode.getText());
        user.setPhoneNumber(labelPhoneNumber.getText());
        user.setEmail(labelEmail.getText());

        var call = userService.save(user);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Platform.runLater( () -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Potwierdzenie");
                        alert.setHeaderText(null);
                        alert.setContentText("Nowe konto użytkownika zostało utworzone!");
                        alert.setX(750);
                        alert.setY(384);
                        alert.showAndWait();

                        SceneCreator.launchScene("LogInScene.fxml");
                    });
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable throwable) {
                errorLabel.setText("Błąd od serwera!" + throwable.getMessage());

            }
        });

    }

}
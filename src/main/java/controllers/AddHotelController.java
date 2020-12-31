package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.HotelService;
import backend.model.Hotel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static utils.Constants.ERROR_EMPTY_VIEW;
import static utils.Constants.ERROR_NOT_A_NUMBER;

public class AddHotelController {
    @FXML public TextField inputName;
    @FXML public TextField inputNumbOfStars;
    @FXML public TextField inputAddress;
    @FXML public TextField inputZipcode;
    @FXML public TextField inputCity;
    @FXML public TextField inputCountry;
    @FXML public Label labelError;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;

    private final HotelService hotelService = AgencyServiceGenerator.createService(HotelService.class);

    @FXML
    public void initialize() {
        setTextFieldsListeners();
    }

    private void setTextFieldsListeners() {
        inputNumbOfStars.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    var isNumber = Integer.parseInt(newValue);
                    labelError.setText("");
                } catch (NumberFormatException e) {
                    labelError.setText(ERROR_NOT_A_NUMBER);
                }
            } else {
                labelError.setText("");
            }
        });
    }

    @FXML
    public void onCancelClick() {
        closeWindow();
    }

    @FXML
    public void onConfirmClick() {
        if (viewsAreNotEmpty() && viewsAreCorrect()) {
            var name = inputName.getText();
            var numbOfStars = Integer.parseInt(inputNumbOfStars.getText());
            var address = inputAddress.getText();
            var zipcode = inputZipcode.getText();
            var city = inputCity.getText();
            var country = inputCountry.getText();

            Hotel newHotel = new Hotel(0, name, numbOfStars, address, zipcode, city, country);

            try {
                hotelService.save(newHotel).execute();
                closeWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            labelError.setText(ERROR_EMPTY_VIEW);
        }
    }

    private boolean viewsAreNotEmpty() {
        return !inputName.getText().isEmpty() && !inputNumbOfStars.getText().isEmpty() &&
                !inputAddress.getText().isEmpty() && !inputZipcode.getText().isEmpty() &&
                !inputCity.getText().isEmpty() && !inputCountry.getText().isEmpty();
    }

    private boolean viewsAreCorrect() {
        return labelError.getText().equals("");
    }

    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }
}

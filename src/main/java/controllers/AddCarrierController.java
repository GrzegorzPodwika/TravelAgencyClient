package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.CarrierService;
import backend.model.Carrier;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static utils.Constants.*;

public class AddCarrierController {
    @FXML public TextField inputName;
    @FXML public TextField inputPhone;
    @FXML public TextField inputEmail;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;
    @FXML public Label labelError;

    private final CarrierService carrierService = AgencyServiceGenerator.createService(CarrierService.class);

    @FXML
    public void initialize() {
        setTextFieldsListeners();
    }

    private void setTextFieldsListeners() {
        inputPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (inputPhone.getText().length() > 9) {
                    String sub = inputPhone.getText().substring(0, 9);
                    inputPhone.setText(sub);
                } else {
                    try {
                        var isNumber = Integer.parseInt(newValue);
                        labelError.setText("");
                    } catch (NumberFormatException e) {
                        labelError.setText(ERROR_NOT_A_NUMBER);
                    }
                }
            } else {
                labelError.setText("");
            }
        });

        inputEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (!newValue.matches("^([a-zA-Z0-9_\\.\\-+])+@[a-zA-Z0-9-.]+\\.[a-zA-Z0-9-]{2,}$")) {
                    labelError.setText(ERROR_NOT_A_EMAIL);
                } else {
                    labelError.setText("");
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

    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void onConfirmClick() {
        if (viewsAreNotEmpty() && viewsAreCorrect()) {
            var name = inputName.getText();
            var phoneNumber = inputPhone.getText();
            var email = inputEmail.getText();

            Carrier newCarrier = new Carrier(0, name, phoneNumber, email);

            try {
                carrierService.save(newCarrier).execute();
                closeWindow();
            } catch (IOException e) {
                e.printStackTrace();
                labelError.setText("Ups.. something gone wrong " + e.getMessage());
            }
        } else {
            labelError.setText(ERROR_EMPTY_VIEW);
        }
    }

    private boolean viewsAreNotEmpty() {
        return inputName.getText() != null && !inputName.getText().isEmpty()
                && inputPhone.getText() != null && !inputPhone.getText().isEmpty()
                && inputEmail.getText() != null && !inputEmail.getText().isEmpty();
    }

    private boolean viewsAreCorrect() {
        return inputPhone.getText().length() == 9 && isNumber(inputPhone) &&
                inputEmail.getText().matches("^([a-zA-Z0-9_\\.\\-+])+@[a-zA-Z0-9-.]+\\.[a-zA-Z0-9-]{2,}$");
    }

    private boolean isNumber(TextField inputPhone) {
        try {
            Integer.parseInt(inputPhone.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

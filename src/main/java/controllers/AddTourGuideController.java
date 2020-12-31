package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.TourGuideService;
import backend.model.Carrier;
import backend.model.TourGuide;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static utils.Constants.*;

public class AddTourGuideController {
    @FXML public TextField inputName;
    @FXML public TextField inputSurname;
    @FXML public TextField inputPhone;
    @FXML public Label labelError;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;

    private final TourGuideService tourGuideService = AgencyServiceGenerator.createService(TourGuideService.class);

    @FXML
    public void initialize() {
        setTextFieldsListeners();
    }

    private void setTextFieldsListeners() {
        inputPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    var isNumber = Integer.parseInt(newValue);
                    if (newValue.length() != 9) {
                        labelError.setText(ERROR_WRONG_PHONE);
                    } else
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
            var surname = inputSurname.getText();
            var phoneNumber = inputPhone.getText();

            TourGuide newTourGuide = new TourGuide(0, name, surname, phoneNumber);

            try {
                tourGuideService.save(newTourGuide).execute();
                closeWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean viewsAreCorrect() {
        return labelError.getText().equals("");
    }

    private boolean viewsAreNotEmpty() {
        return !inputName.getText().isEmpty() && !inputSurname.getText().isEmpty()
                && !inputPhone.getText().isEmpty();
    }

    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }
}

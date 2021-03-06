package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.TourGuideService;
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

        inputName.textProperty().addListener((observableValue, oldValue, newValue) -> {

            if (newValue != null && !newValue.isEmpty()) {
                if (!newValue.matches("^[A-Z]([a-z])+$"))
                    labelError.setText(ERROR_CAPITALIZE_WORD);
                else
                    labelError.setText("");
            } else {
                labelError.setText("");
            }
        } );

        inputSurname.textProperty().addListener((observableValue, oldValue, newValue) -> {

            if (newValue != null && !newValue.isEmpty()) {
                if (!newValue.matches("^[A-Z]([a-z])+$"))
                    labelError.setText(ERROR_CAPITALIZE_WORD);
                else
                    labelError.setText("");
            } else {
                labelError.setText("");
            }
        } );
    }

    @FXML
    public void onCancelClick() {
        closeWindow();
    }

    @FXML
    public void onConfirmClick() {
        if (viewsAreNotEmpty()) {
            if (phoneIsCorrect()) {
                if (nameAndSurnameIsCorrect()) {
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
                } else {
                    labelError.setText(ERROR_CAPITALIZE_WORD);
                }
            } else {
                labelError.setText(ERROR_WRONG_PHONE);
            }
        } else {
            labelError.setText(ERROR_EMPTY_VIEW);
        }
    }

    private boolean nameAndSurnameIsCorrect() {
        return inputName.getText().matches("^[A-Z]([a-z])+$") && inputSurname.getText().matches("^[A-Z]([a-z])+$");
    }

    private boolean viewsAreNotEmpty() {
        return inputName.getText() != null && !inputName.getText().isEmpty()
                && inputSurname.getText() != null && !inputSurname.getText().isEmpty()
                && inputPhone.getText() != null && !inputPhone.getText().isEmpty();
    }

    private boolean phoneIsCorrect() {
        try {
            var isNumber = Integer.parseInt(inputPhone.getText());
            return inputPhone.getText().length() == 9;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }
}

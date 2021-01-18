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

import static utils.Constants.ERROR_NOT_A_NUMBER;
import static utils.Constants.ERROR_WRONG_PHONE;

public class EditTourGuideController {
    @FXML public TextField inputName;
    @FXML public TextField inputSurname;
    @FXML public TextField inputPhone;
    @FXML public Label labelError;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;
    @FXML public Label labelName;
    @FXML public Label labelSurname;
    @FXML public Label labelPhone;

    private final TourGuideService tourGuideService = AgencyServiceGenerator.createService(TourGuideService.class);
    private final TourGuide fetchedTourGuide = Main.getTourGuide();

    @FXML
    public void initialize() {
        initLabels();
        setTextFieldsListeners();
    }

    private void initLabels() {
        labelName.setText(labelName.getText() + fetchedTourGuide.getName());
        labelSurname.setText(labelSurname.getText() + fetchedTourGuide.getSurname());
        labelPhone.setText(labelPhone.getText() + fetchedTourGuide.getPhoneNumber());
    }

    private void setTextFieldsListeners() {
        inputPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (inputPhone.getText().length() > 9) {
                    String sub = inputPhone.getText().substring(0, 9);
                    inputPhone.setText(sub);
                } else {
                    try {
                        Integer.parseInt(newValue);
                        labelError.setText("");
                    } catch (NumberFormatException e) {
                        labelError.setText(ERROR_NOT_A_NUMBER);
                    }
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
        TourGuide tourGuideToUpdate = generateProperTourGuide();

        try {
            tourGuideService.update(tourGuideToUpdate).execute();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private TourGuide generateProperTourGuide() {
        TourGuide tourGuideToUpdate = new TourGuide();
        tourGuideToUpdate.setTourGuideId(fetchedTourGuide.getTourGuideId());

        if (inputName.getText().isEmpty())
            tourGuideToUpdate.setName(fetchedTourGuide.getName());
        else
            tourGuideToUpdate.setName(inputName.getText());

        if (inputSurname.getText().isEmpty())
            tourGuideToUpdate.setSurname(fetchedTourGuide.getSurname());
        else
            tourGuideToUpdate.setSurname(inputSurname.getText());

        if (inputPhone.getText().isEmpty() || !phoneIsCorrect()) {
            tourGuideToUpdate.setPhoneNumber(fetchedTourGuide.getPhoneNumber());
        }
        else {
            tourGuideToUpdate.setPhoneNumber(inputPhone.getText());
        }


        return tourGuideToUpdate;
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

package controllers;

import backend.api.AdditionalServiceService;
import backend.api.AgencyServiceGenerator;
import backend.model.AdditionalService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddAdditionalServiceController {
    @FXML public TextField inputName;
    @FXML public TextField inputDescription;
    @FXML public Label labelError;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;

    private final AdditionalServiceService additionalServiceService = AgencyServiceGenerator.createService(AdditionalServiceService.class);

    @FXML
    public void onCancelClick() {
        closeWindow();
    }

    @FXML
    public void onConfirmClick() {
        if (viewsAreNotEmpty()) {
            var name = inputName.getText();
            var description = inputDescription.getText();

            AdditionalService additionalService = new AdditionalService();
            additionalService.setAdditionalServiceId(0);
            additionalService.setName(name);
            additionalService.setDescription(description);

            try {
                additionalServiceService.save(additionalService).execute();
                closeWindow();
            } catch (IOException e) {
                e.printStackTrace();
                labelError.setText("Ups.. something gone wrong " + e.getMessage());
            }
        } else {
            labelError.setText("Wypełnij wszystkie pola!");
        }
    }

    private boolean viewsAreNotEmpty() {
        return inputName.getText() != null && !inputName.getText().isEmpty()
                && inputDescription.getText() != null && !inputDescription.getText().isEmpty();
    }


    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }
}

package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.AttractionService;
import backend.model.Attraction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddAttractionController {
    @FXML public TextField inputName;
    @FXML public TextField inputDescription;
    @FXML public Label labelError;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;

    private final AttractionService attractionService = AgencyServiceGenerator.createService(AttractionService.class);

    @FXML
    public void onCancelClick() {
        closeWindow();
    }

    @FXML
    public void onConfirmClick() {
        if (viewsAreNotEmpty()) {
            var name = inputName.getText();
            var description = inputDescription.getText();

            Attraction attraction = new Attraction();
            attraction.setAttractionId(0);
            attraction.setName(name);
            attraction.setDescription(description);

            try {
                attractionService.save(attraction).execute();
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

package controllers;

import backend.api.AdditionalServiceService;
import backend.api.AgencyServiceGenerator;
import backend.api.AttractionService;
import backend.model.AdditionalService;
import backend.model.Attraction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditAttractionController {
    @FXML public TextField inputName;
    @FXML public TextField inputDescription;
    @FXML public Label labelError;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;
    @FXML public Label labelName;
    @FXML public Label labelDescription;

    private final AttractionService attractionService = AgencyServiceGenerator.createService(AttractionService.class);
    private final Attraction fetchedAttraction = Main.getAttraction();

    @FXML
    public void initialize() {
        initLabels();
    }

    private void initLabels() {
        labelName.setText(labelName.getText() + fetchedAttraction.getName());
        labelDescription.setText(labelDescription.getText() + fetchedAttraction.getDescription());
    }

    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void onCancelClick() {
        closeWindow();
    }


    @FXML
    public void onConfirmClick() {
        Attraction attractionToUpdate = generateAttraction();

        try {
            attractionService.update(attractionToUpdate).execute();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            labelError.setText("Ups.. something gone wrong " + e.getMessage());
        }
    }

    private Attraction generateAttraction() {
        Attraction attractionToUpdate = new Attraction();
        attractionToUpdate.setAttractionId(fetchedAttraction.getAttractionId());

        if (inputName.getText() == null || inputName.getText().isEmpty())
            attractionToUpdate.setName(fetchedAttraction.getName());
        else
            attractionToUpdate.setName(inputName.getText());

        if (inputDescription.getText() == null || inputDescription.getText().isEmpty())
            attractionToUpdate.setDescription(fetchedAttraction.getDescription());
        else
            attractionToUpdate.setDescription(inputDescription.getText());

        return attractionToUpdate;
    }
}

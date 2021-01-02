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

public class EditAdditionalServiceController {

    @FXML public TextField inputName;
    @FXML public TextField inputDescription;
    @FXML public Label labelError;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;
    @FXML public Label labelName;
    @FXML public Label labelDescription;

    private final AdditionalServiceService additionalServiceService = AgencyServiceGenerator.createService(AdditionalServiceService.class);
    private final AdditionalService fetchedService = Main.getAdditionalService();

    @FXML
    public void initialize() {
        initLabels();
    }

    private void initLabels() {
        labelName.setText(labelName.getText() + fetchedService.getName());
        labelDescription.setText(labelDescription.getText() + fetchedService.getDescription());
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
        AdditionalService serviceToUpdate = generateAdditionalService();

        try {
            additionalServiceService.update(serviceToUpdate).execute();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            labelError.setText("Ups.. something gone wrong " + e.getMessage());
        }
    }

    private AdditionalService generateAdditionalService() {
        AdditionalService serviceToUpdate = new AdditionalService();
        serviceToUpdate.setAdditionalServiceId(fetchedService.getAdditionalServiceId());

        if (inputName.getText() == null || inputName.getText().isEmpty())
            serviceToUpdate.setName(fetchedService.getName());
        else
            serviceToUpdate.setName(inputName.getText());

        if (inputDescription.getText() == null || inputDescription.getText().isEmpty())
            serviceToUpdate.setDescription(fetchedService.getDescription());
        else
            serviceToUpdate.setDescription(inputDescription.getText());

        return serviceToUpdate;
    }

}

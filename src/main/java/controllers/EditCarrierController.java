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

public class EditCarrierController {

    @FXML public TextField inputName;
    @FXML public TextField inputPhone;
    @FXML public TextField inputEmail;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;
    @FXML public Label labelError;
    @FXML public Label labelName;
    @FXML public Label labelPhone;
    @FXML public Label labelEmail;

    private final CarrierService carrierService = AgencyServiceGenerator.createService(CarrierService.class);
    private final Carrier fetchedCarrier = Main.getCarrier();

    @FXML
    public void initialize() {
        initLabels();
        setTextFieldsListeners();
    }

    private void initLabels() {
        labelName.setText(labelName.getText() + fetchedCarrier.getName());
        labelPhone.setText(labelPhone.getText() + fetchedCarrier.getPhoneNumber());
        labelEmail.setText(labelEmail.getText() + fetchedCarrier.getEmail());
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
                        if (newValue.length() != 9) {
                            labelError.setText(ERROR_WRONG_PHONE);
                        } else
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
        Carrier carrierToUpdate = generateProperCarrier();

        try {
            carrierService.update(carrierToUpdate).execute();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Carrier generateProperCarrier() {
        Carrier carrierToUpdate = new Carrier();
        carrierToUpdate.setCarrierId(fetchedCarrier.getCarrierId());

        if (inputName.getText() == null || inputName.getText().isEmpty())
            carrierToUpdate.setName(fetchedCarrier.getName());
        else
            carrierToUpdate.setName(inputName.getText());

        if (inputPhone.getText() == null || inputPhone.getText().isEmpty() || !phoneIsCorrect())
            carrierToUpdate.setPhoneNumber(fetchedCarrier.getPhoneNumber());
        else
            carrierToUpdate.setPhoneNumber(inputPhone.getText());

        if (inputEmail.getText() == null || inputEmail.getText().isEmpty() || !emailIsCorrect())
            carrierToUpdate.setEmail(fetchedCarrier.getEmail());
        else
            carrierToUpdate.setEmail(inputEmail.getText());

        return carrierToUpdate;
    }

    private boolean phoneIsCorrect() {
        try {
            var isNumber = Integer.parseInt(inputPhone.getText());
            return inputPhone.getText().length() == 9;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean emailIsCorrect() {
        return inputEmail.getText().matches("^([a-zA-Z0-9_\\.\\-+])+@[a-zA-Z0-9-.]+\\.[a-zA-Z0-9-]{2,}$");
    }

}

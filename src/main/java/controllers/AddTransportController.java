package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.CarrierService;
import backend.api.TransportService;
import backend.model.Carrier;
import backend.model.Transport;
import backend.model.TransportType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Constants.ERROR_EMPTY_VIEW;
import static utils.Constants.ERROR_NOT_A_NUMBER;

public class AddTransportController {
    @FXML public TextField inputJourneyTime;
    @FXML public TextField inputJourneyDescription;
    @FXML public ComboBox<String> comboBoxTransportType;
    @FXML public ComboBox<Carrier> comboBoxCarrier;

    @FXML public Label labelError;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;

    private final CarrierService carrierService = AgencyServiceGenerator.createService(CarrierService.class);
    private final TransportService transportService = AgencyServiceGenerator.createService(TransportService.class);
    private final ObservableList<Carrier> observableCarriers = FXCollections.observableArrayList();
    private final List<String> journeyTypes = Arrays.stream(TransportType.values())
            .map(Enum::name).collect(Collectors.toList());


    @FXML
    public void initialize() {
        setTextFieldsListeners();
        setObjectConverterInComboBox();
        fetchAllCarriers();
        setUpTransportTypeComboBox();
    }

    private void setTextFieldsListeners() {
        inputJourneyTime.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    var parsedNumber = Double.parseDouble(newValue);
                    labelError.setText("");
                } catch (NumberFormatException e) {
                    labelError.setText(ERROR_NOT_A_NUMBER);
                }
            } else {
                labelError.setText("");
            }
        });

    }

    private void setObjectConverterInComboBox() {
        comboBoxCarrier.setConverter(new StringConverter<Carrier>() {
            @Override
            public String toString(Carrier carrier) {
                return carrier.getName();
            }

            @Override
            public Carrier fromString(String s) {
                return comboBoxCarrier.getItems().stream()
                        .filter(carrier -> carrier.getName().equals(s)).findFirst().orElse(null);
            }
        });
    }

    private void fetchAllCarriers() {
        var fetchCarriersCall = carrierService.getAll();
        fetchCarriersCall.enqueue(new Callback<List<Carrier>>() {
            @Override
            public void onResponse(Call<List<Carrier>> call, Response<List<Carrier>> response) {
                if (response.isSuccessful()) {
                    var carriers = response.body();

                    if (carriers != null) {
                        observableCarriers.addAll(carriers);
                        Platform.runLater(() -> {
                            comboBoxCarrier.setItems(observableCarriers);
                        });
                    } else {
                        System.out.println("List of carriers == null");
                    }
                } else {
                    System.out.println("Response was not successful, code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Carrier>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());
            }
        });
    }

    private void setUpTransportTypeComboBox() {
        comboBoxTransportType.getItems().addAll(journeyTypes);
    }

    @FXML
    public void onCancelClick() {
        closeWindow();
    }

    @FXML
    public void onConfirmClick() {
        if (inputsAreNotEmpty() && journeyTimeIsNumber()) {
            var transportType = comboBoxTransportType.getSelectionModel().getSelectedItem();
            var journeyTime = Double.parseDouble(inputJourneyTime.getText());
            var journeyDescription = inputJourneyDescription.getText();
            var selectedCarrier = comboBoxCarrier.getSelectionModel().getSelectedItem();

            Transport transport = new Transport(0, transportType, journeyTime, journeyDescription, selectedCarrier);

            try {
                transportService.save(transport).execute();
                closeWindow();
            } catch (IOException e) {
                e.printStackTrace();
                labelError.setText("Ups.. something gone wrong " + e.getMessage());
            }

        } else {
            labelError.setText(ERROR_EMPTY_VIEW);
        }
    }

    private boolean inputsAreNotEmpty() {
        return inputJourneyTime.getText() != null && !inputJourneyTime.getText().isEmpty()
                && inputJourneyDescription.getText() != null && !inputJourneyDescription.getText().isEmpty()
                && !comboBoxTransportType.getSelectionModel().isEmpty() && !comboBoxCarrier.getSelectionModel().isEmpty();
    }

    private boolean journeyTimeIsNumber() {
        try {
            var parsedNumber = Double.parseDouble(inputJourneyTime.getText());
            labelError.setText("");
            return true;
        } catch (NumberFormatException e) {
            labelError.setText(ERROR_NOT_A_NUMBER);
            return false;
        }
    }

    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }

}
